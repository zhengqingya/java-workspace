package com.zhengqing.demo.api;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/10/5 2:36 下午
 */
@Slf4j
@RestController
@RequestMapping("")
@Api(tags = "测试api")
public class TestController {

    ThreadLocal<String> TTL = new TransmittableThreadLocal<>();

    @GetMapping("time")
    @ApiOperation("time")
    public String time() {
        log.info("time: {}", DateUtil.date());
        return DateUtil.date().toString();
    }

    @GetMapping("ttl_set")
    @ApiOperation("ttl_set")
    public Object ttl_set() {
        String threadName = Thread.currentThread().getName();
        TTL.set(threadName);
        log.info("ttl_set: {}", threadName);
        return TTL.get();
    }

    @GetMapping("ttl_get")
    @ApiOperation("ttl_get")
    public Object ttl_get() {
        log.info("ttl_get: {}", TTL.get());
        return TTL.get();
    }

    @GetMapping("ttl_test")
    @ApiOperation("ttl_test")
    public Object ttl_test() {
        TTL.set("hello");
        // 收集多个异步任务的结果
        List<CompletableFuture<String>> cfList = Lists.newArrayList();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            // supplyAsync: 提交每个任务到线程池中执行
            CompletableFuture<String> cfItem = CompletableFuture.supplyAsync(() -> {
                log.info("【start】 executor index:{} value: {}", finalI, TTL.get());
                TTL.set("hello：" + finalI);
                ThreadUtil.sleep(100, TimeUnit.MILLISECONDS);
                log.info("executor index:{} value: {}", finalI, TTL.get());
                return TTL.get();
            }, TtlExecutors.getTtlExecutorService(executor)); // 使用 executor 不会自动清理 ThreadLocal 的值，ttl包裹一下可以达到自动清理
            cfList.add(cfItem);
        }

        // 异步阻塞，等待所有任务执行完成
        CompletableFuture.allOf(cfList.toArray(new CompletableFuture[0])).join();

        // 获取所有结果
        List<String> results = cfList.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        executor.shutdown();

        log.info("final value: {}", TTL.get());
        TTL.remove();
        return results;
    }

}
