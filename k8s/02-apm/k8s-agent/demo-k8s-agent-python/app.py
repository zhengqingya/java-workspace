import logging
import os

import httpx
import uvicorn
from fastapi import FastAPI

try:
    from skywalking.trace.context import get_context
except ImportError:
    get_context = None


SERVICE_NAME = os.getenv("SW_AGENT_NAME", "demo-k8s-agent-python")
DEFAULT_HTTP_ADDR = "0.0.0.0"
DEFAULT_HTTP_PORT = 18081


class TraceLogFallbackFilter(logging.Filter):
    def filter(self, record):
        # 1、未通过 SkyWalking Python Agent 启动时，补齐日志格式中需要的服务字段。
        record.swServiceName = getattr(record, "swServiceName", os.getenv("SW_AGENT_NAME", SERVICE_NAME))
        return True


logging.basicConfig(
    level=os.getenv("LOG_LEVEL", "INFO").upper(),
    format="%(asctime)s %(levelname)s [service=%(swServiceName)s] [%(name)s] %(message)s",
)
for handler in logging.getLogger().handlers:
    handler.addFilter(TraceLogFallbackFilter())

logger = logging.getLogger(SERVICE_NAME)
logger.addFilter(TraceLogFallbackFilter())
app = FastAPI(title=SERVICE_NAME)


@app.get("/hello")
async def hello(name: str = "python"):
    trace_context = current_trace_context()
    logger.info("hello request, name=%s %s", name, trace_context["log_text"])
    return {
        "message": f"hello, {name}",
        "service": SERVICE_NAME,
        "trace_id": trace_context["trace_id"],
    }


@app.get("/chain")
async def chain(targetName: str = "target", targetUrl: str = ""):
    targetName = targetName.strip() or "target"
    targetUrl = targetUrl.strip()
    trace_context = current_trace_context()
    logger.info("chain request, target_name=%s, target_url=%s %s", targetName, targetUrl, trace_context["log_text"])

    result = {
        "service": SERVICE_NAME,
        "message": f"python -> {targetName}",
        "target_name": targetName,
        "target_url": targetUrl,
        "trace_id": trace_context["trace_id"],
    }
    if not targetUrl:
        return result

    async with httpx.AsyncClient(timeout=5.0) as client:
        response = await client.get(targetUrl)
        response.raise_for_status()

    result["downstream"] = response.json()
    return result


def current_trace_context():
    # 1、没有通过 SkyWalking Python Agent 启动时，返回固定占位值，避免本地直接运行报错。
    if get_context is None:
        return build_trace_context()

    # 2、从当前请求 span 中读取 SkyWalking trace 上下文，用于接口响应和控制台日志。
    context = get_context()
    span = context.active_span
    if span is None:
        return build_trace_context()

    # 3、trace_id 用于 UI 检索链路，segment_id/span_id 用于排查当前服务内的调用片段。
    trace_id = str(context.segment.related_traces[0]) if context.segment.related_traces else "N/A"
    segment_id = str(context.segment.segment_id)
    span_id = str(span.sid)
    return build_trace_context(trace_id, segment_id, span_id)


def build_trace_context(trace_id="N/A", segment_id="N/A", span_id="N/A"):
    return {
        "trace_id": trace_id,
        "segment_id": segment_id,
        "span_id": span_id,
        "log_text": f"trace_id={trace_id} segment_id={segment_id} span_id={span_id}",
    }


if __name__ == "__main__":
    uvicorn.run(
        app,
        host=os.getenv("HTTP_HOST", DEFAULT_HTTP_ADDR),
        port=int(os.getenv("HTTP_PORT", str(DEFAULT_HTTP_PORT))),
    )
