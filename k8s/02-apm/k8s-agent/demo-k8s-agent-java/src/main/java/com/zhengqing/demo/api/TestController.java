package com.zhengqing.demo.api;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
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

    private static final String SERVICE_NAME = StrUtil.blankToDefault(System.getenv("SW_AGENT_NAME"), "demo-k8s-agent-java");

    /**
     * 单服务请求验证。
     */
    @GetMapping("hello") // http://127.0.0.1/hello
    public Map<String, Object> hello(@RequestParam(defaultValue = "skywalking") String name) {
        // 1、记录当前请求参数，方便从控制台日志观察 trace_id。
        log.info("hello request, name={}", name);

        // 2、读取 SkyWalking Java Agent 注入的当前链路 trace_id。
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "hello, " + name);
        result.put("service", SERVICE_NAME);
        result.put("trace_id", TraceContext.traceId());
        // 3、返回统一字段 trace_id，方便和 Python/Go/PHP 响应对齐。
        return result;
    }

    /**
     * 链路请求验证。
     */
    @GetMapping("chain") // http://127.0.0.1/chain?targetName=python&targetUrl=http://127.0.0.1:18081/hello?name=from-java
    @ApiOperation("java -> target")
    public Map<String, Object> chain(@RequestParam(defaultValue = "target") String targetName,
                                     @RequestParam(required = false) String targetUrl) {
        // 1、补齐默认目标服务名称。
        targetName = StrUtil.blankToDefault(targetName, "target");
        log.info("chain request, targetName={}, targetUrl={}", targetName, targetUrl);

        // 2、先返回当前服务链路信息；没有下游地址时只验证当前服务。
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("service", SERVICE_NAME);
        result.put("message", "java -> " + targetName);
        result.put("target_name", targetName);
        result.put("target_url", targetUrl);
        result.put("trace_id", TraceContext.traceId());

        if (StrUtil.isBlank(targetUrl)) {
            return result;
        }

        // 3、请求下游服务，SkyWalking Java Agent 会自动透传链路上下文。
        String targetResponse = HttpUtil.get(targetUrl);
        result.put("downstream", JSONUtil.parseObj(targetResponse));
        return result;
    }

    /**
     * 链路末端请求验证。
     */
    @GetMapping("trace/final") // http://127.0.0.1/trace/final
    @ApiOperation("print tracing headers at the java final hop")
    public Map<String, Object> traceFinal(HttpServletRequest request) {
        // 1、收集进入 Java 末端服务的请求头，验证跨语言链路透传。
        Map<String, String> headers = new LinkedHashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        log.info("trace final reached, headers={}", headers);

        // 2、读取当前请求所在链路的 trace_id。
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("service", SERVICE_NAME);
        result.put("message", "java final reached");
        result.put("path", request.getRequestURI());
        result.put("trace_id", TraceContext.traceId());
        // 3、保留 headers，方便排查 sw8 等跨服务链路头是否透传。
        result.put("headers", headers);
        return result;
    }

}
