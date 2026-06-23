package com.zhengqing.demo.api;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
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

        if (StrUtil.isBlank(targetUrl)) {
            return result;
        }

        String targetResponse = HttpUtil.get(targetUrl);
        result.put("downstream", JSONUtil.parseObj(targetResponse));
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
        return result;
    }

}
