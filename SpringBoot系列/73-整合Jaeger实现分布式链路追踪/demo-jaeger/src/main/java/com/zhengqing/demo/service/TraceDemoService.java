package com.zhengqing.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraceDemoService {

    // Sleuth 注入的 tracer，用来读取当前 span，并手工创建业务子 span。
    private final Tracer tracer;

    public String traceFlow() {
        // 当前 HTTP 请求进入 Controller 后，Sleuth 已经帮我们创建好了入口 span。
        Span parent = tracer.currentSpan();
        log.info("receive trace request, traceId={}, spanId={}",
                parent != null ? parent.context().traceId() : "N/A",
                parent != null ? parent.context().spanId() : "N/A");

        // 手工补一个业务 span，方便在 Jaeger UI 中观察业务处理阶段。
        Span child = tracer.nextSpan().name("custom-business-span");
        try (Tracer.SpanInScope ignored = tracer.withSpan(child.start())) {
            log.info("run custom business span");
            mockRemoteCall();
        } finally {
            child.end();
        }

        return "trace success";
    }

    private void mockRemoteCall() {
        // 再补一个更细粒度的子 span，模拟一次下游调用。
        Span child = tracer.nextSpan().name("mock-remote-call");
        try (Tracer.SpanInScope ignored = tracer.withSpan(child.start())) {
            log.info("simulate downstream call");
        } finally {
            child.end();
        }
    }
}
