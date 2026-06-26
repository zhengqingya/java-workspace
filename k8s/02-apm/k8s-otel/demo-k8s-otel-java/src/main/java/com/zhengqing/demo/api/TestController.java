package com.zhengqing.demo.api;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("")
@Api(tags = "测试api")
public class TestController {

    private static final String SERVICE_NAME = "demo-k8s-otel-java";

    @GetMapping("hello") // http://127.0.0.1/hello
    public Map<String, Object> hello(@RequestParam(defaultValue = "skywalking") String name) {
        log.info("hello request, name={}", name);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "hello, " + name);
        result.put("service", SERVICE_NAME);
        result.putAll(this.currentTraceContext());
        return result;
    }

    @GetMapping("chain") // http://127.0.0.1/chain?targetName=python&targetUrl=http://127.0.0.1:18081/hello?name=from-java
    @ApiOperation("java -> target")
    public Map<String, Object> chain(@RequestParam(defaultValue = "target") String targetName,
                                     @RequestParam(required = false) String targetUrl) {
        targetName = StrUtil.blankToDefault(targetName, "target");
        log.info("chain request, targetName={}, targetUrl={}", targetName, targetUrl);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("service", SERVICE_NAME);
        result.put("message", "java -> " + targetName);
        result.put("target_name", targetName);
        result.put("target_url", targetUrl);
        result.putAll(this.currentTraceContext());

        if (StrUtil.isBlank(targetUrl)) {
            return result;
        }

        String targetResponse = HttpUtil.get(targetUrl);
        result.put("downstream", JSONUtil.parseObj(targetResponse));
        return result;
    }

    /**
     * 验证异常接口。
     *
     * @param random 是否随机异常
     * @param rate   随机异常概率，取值范围：0~100
     * @return 未触发异常时的响应信息
     */
    @GetMapping("error") // http://127.0.0.1/error?random=true&rate=50
    @ApiOperation("验证异常接口")
    public Map<String, Object> error(@RequestParam(defaultValue = "true") Boolean random,
                                     @RequestParam(defaultValue = "50") Integer rate) {
        // 1、修正异常概率，避免传入负数或超过 100 的值影响随机判断。
        int finalRate = Math.max(0, Math.min(100, rate));
        String now = DateUtil.now();
        log.info("[{}] 异常验证请求，random={}，rate={}，finalRate={}", now, random, rate, finalRate);

        // 2、根据请求参数决定是否触发异常，未捕获异常会直接形成 500，方便 APM 统计异常接口。
        boolean triggerError = !Boolean.TRUE.equals(random) || RandomUtil.randomInt(100) < finalRate;
        if (triggerError) {
            log.error("[{}] 触发异常验证，random={}，finalRate={}", now, random, finalRate);
            throw new RuntimeException("异常验证接口主动抛出异常");
        }

        // 3、未触发异常时返回正常响应和当前链路标识。
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("service", SERVICE_NAME);
        result.put("time", now);
        result.putAll(this.currentTraceContext());
        return result;
    }

    /**
     * 验证耗时接口。
     *
     * @param sleepMs 睡眠毫秒数，取值范围：0~30000
     * @return 耗时响应信息
     */
    @GetMapping("slow") // http://127.0.0.1/slow?sleepMs=3000
    @ApiOperation("验证耗时接口")
    public Map<String, Object> slow(@RequestParam(defaultValue = "1000") Long sleepMs) {
        // 1、修正睡眠时间，避免误传过大数值长时间占用请求线程。
        long finalSleepMs = Math.max(0L, Math.min(30000L, sleepMs));
        log.info("耗时验证请求，sleepMs={}，finalSleepMs={}", sleepMs, finalSleepMs);

        // 2、按修正后的毫秒数睡眠，线程中断时恢复中断标记并抛出异常，便于 APM 标记错误。
        try {
            Thread.sleep(finalSleepMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("耗时验证接口被中断", e);
        }

        // 3、返回实际睡眠时间和当前链路标识。
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("service", SERVICE_NAME);
        result.put("message", "java slow check passed");
        result.put("sleep_ms", finalSleepMs);
        result.putAll(this.currentTraceContext());
        return result;
    }

    @GetMapping("trace/final") // http://127.0.0.1/trace/final
    @ApiOperation("print tracing headers at the java final hop")
    public Map<String, Object> traceFinal(HttpServletRequest request) {
        Map<String, String> headers = new LinkedHashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        log.info("trace final reached, headers={}", headers);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("service", SERVICE_NAME);
        result.put("message", "java final reached");
        result.put("path", request.getRequestURI());
        result.put("headers", headers);
        result.putAll(this.currentTraceContext());
        return result;
    }

    /**
     * 获取当前请求链路上下文。
     *
     * @return trace_id 和 span_id
     */
    private Map<String, Object> currentTraceContext() {
        // 1、读取 OpenTelemetry Java Agent 自动注入到当前线程的 Span 上下文。
        SpanContext spanContext = Span.current().getSpanContext();

        // 2、本地未挂载 Java Agent 或当前请求无有效 Span 时，返回固定兜底值，保持接口结构稳定。
        Map<String, Object> traceContext = new LinkedHashMap<>();
        if (!spanContext.isValid()) {
            traceContext.put("trace_id", "0");
            traceContext.put("span_id", "0");
            return traceContext;
        }

        // 3、按 OTel/SigNoz/Grafana 常见十六进制格式返回链路标识。
        traceContext.put("trace_id", spanContext.getTraceId());
        traceContext.put("span_id", spanContext.getSpanId());
        return traceContext;
    }

}
