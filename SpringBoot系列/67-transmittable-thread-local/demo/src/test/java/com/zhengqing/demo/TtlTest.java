package com.zhengqing.demo;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class TtlTest {
    public static void main(String[] args) {
        ThreadLocal<String> TTL = new TransmittableThreadLocal<>();
        TTL.set("hello");
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> {
            TTL.set("hello-111");
            log.info("executor value: {}", TTL.get()); // "executor" 线程复用时，如果ThreadLocal中的值未remove，之后的相同线程会拿到这个线程设置的值
        });

        log.info("value: {}", TTL.get());

        TtlExecutors.getTtlExecutorService(executor).submit(() -> {
            TTL.set("hello-222");
            log.info("ttl value: {}", TTL.get());  // "ttl" 线程复用时，如果ThreadLocal中的值未remove，ttl方式会自动remove，不会保留
        });

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            executor.submit(() -> {
                log.info("executor index:{} value: {}", finalI, TTL.get()); // executor
            });
            TtlExecutors.getTtlExecutorService(executor).submit(() -> {
                log.info("ttl index:{} value: {}", finalI, TTL.get()); // ttl
            });
        }

        log.info("value: {}", TTL.get());
        executor.shutdown();
    }

}
