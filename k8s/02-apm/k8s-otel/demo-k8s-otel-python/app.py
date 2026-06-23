import logging
import os

import httpx
import uvicorn
from fastapi import FastAPI
from opentelemetry import trace

SERVICE_NAME = "demo-k8s-otel-python"
DEFAULT_HTTP_ADDR = "0.0.0.0"
DEFAULT_HTTP_PORT = 18081


class TraceLogFallbackFilter(logging.Filter):
    def filter(self, record):
        # 1、未通过 OTel 自动注入启动时，补齐日志格式中需要的链路字段。
        record.otelTraceID = getattr(record, "otelTraceID", "0")
        record.otelSpanID = getattr(record, "otelSpanID", "0")
        record.otelTraceSampled = getattr(record, "otelTraceSampled", "false")
        record.otelServiceName = getattr(record, "otelServiceName", SERVICE_NAME)
        return True


logging.basicConfig(
    level=os.getenv("LOG_LEVEL", "INFO").upper(),
    format="%(asctime)s %(levelname)s [trace_id=%(otelTraceID)s span_id=%(otelSpanID)s sampled=%(otelTraceSampled)s service=%(otelServiceName)s] [%(name)s] %(message)s",
)
for handler in logging.getLogger().handlers:
    handler.addFilter(TraceLogFallbackFilter())

logger = logging.getLogger(SERVICE_NAME)
logger.addFilter(TraceLogFallbackFilter())
app = FastAPI(title=SERVICE_NAME)


def current_trace_context():
    # 1、从 OTel 自动注入创建的当前 Span 中读取链路上下文。
    span_context = trace.get_current_span().get_span_context()
    if not span_context.is_valid:
        return {
            "trace_id": "0",
            "span_id": "0",
        }

    # 2、按 SkyWalking/OTel UI 常见的十六进制格式输出 trace_id 和 span_id。
    return {
        "trace_id": format(span_context.trace_id, "032x"),
        "span_id": format(span_context.span_id, "016x"),
    }


@app.get("/hello")
async def hello(name: str = "python"):
    logger.info("hello request, name=%s", name)
    return {
        "message": f"hello, {name}",
        "service": SERVICE_NAME,
        **current_trace_context(),
    }


@app.get("/chain")
async def chain(targetName: str = "target", targetUrl: str = ""):
    targetName = targetName.strip() or "target"
    targetUrl = targetUrl.strip()
    logger.info("chain request, target_name=%s, target_url=%s", targetName, targetUrl)

    result = {
        "service": SERVICE_NAME,
        "message": f"python -> {targetName}",
        "target_name": targetName,
        "target_url": targetUrl,
        **current_trace_context(),
    }
    if not targetUrl:
        return result

    async with httpx.AsyncClient(timeout=5.0) as client:
        response = await client.get(targetUrl)
        response.raise_for_status()

    result["downstream"] = response.json()
    return result


if __name__ == "__main__":
    uvicorn.run(
        app,
        host=os.getenv("HTTP_HOST", DEFAULT_HTTP_ADDR),
        port=int(os.getenv("HTTP_PORT", str(DEFAULT_HTTP_PORT))),
    )
