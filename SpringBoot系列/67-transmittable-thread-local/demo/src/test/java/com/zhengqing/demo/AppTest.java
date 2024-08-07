package com.zhengqing.demo;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * <p> 测试 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/8/5 14:34
 */
@Slf4j
public class AppTest {


    @Test
    public void test_01() throws Exception {
        ThreadLocal<Integer> COUNTER = new ThreadLocal<>();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 100; i++) {
            int index = i;
            executorService.submit(() -> {
                COUNTER.set(index);
                if (index != COUNTER.get()) {
                    log.error("index:{} counter:{}", index, COUNTER.get());
                } else {
                    log.info("index:{} counter:{}", index, COUNTER.get());
                }
                COUNTER.remove();
            });
        }
        executorService.shutdown();
        ThreadUtil.sleep(3, TimeUnit.SECONDS);
    }

    @Test
    public void test_02() throws Exception {
        ThreadLocal<Integer> TL = new ThreadLocal<>();
        TL.set(1);
        ExecutorService executorService = ThreadUtil.newExecutor(3);
        executorService.execute(() -> {
            log.info("TL:{}", TL.get()); // null
        });
        ThreadUtil.sleep(1, TimeUnit.SECONDS);
    }

    @Test
    public void test_03() throws Exception {
        ThreadLocal<Integer> TTL = new TransmittableThreadLocal<>();
        TTL.set(1);
        ExecutorService executorService = ThreadUtil.newExecutor(3);
        executorService.execute(() -> {
            log.info("TTL:{}", TTL.get()); // 1
        });
        TtlExecutors.getTtlExecutorService(executorService).execute(() -> {
            log.info("TTL:{}", TTL.get()); // 1
        });
        ThreadUtil.sleep(1, TimeUnit.SECONDS);
    }

    @Test
    public void test_04() throws Exception {
        ThreadLocal<String> TTL = new TransmittableThreadLocal<>();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CompletionService<String> completionService = new ExecutorCompletionService<>(executor);

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            completionService.submit(() -> {
                TTL.set("set-" + finalI);
                log.info("executor value: {}", TTL.get());
                return TTL.get();
            });
        }

        for (int i = 0; i < 5; i++) {
            Future<String> result = completionService.take();
            log.info("result-{}: {}", i, result.get());
        }

        log.info("value: {}", TTL.get());
    }

    @Test
    public void test_05() throws Exception {
        // 收集多个异步任务的结果
        List<CompletableFuture<String>> cfList = Lists.newArrayList();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 5; i++) {
            // supplyAsync: 提交每个任务到线程池中执行
            CompletableFuture<String> cfItem = CompletableFuture.supplyAsync(() -> {
                ThreadUtil.sleep(100,TimeUnit.MILLISECONDS);
                return "hello";
            }, executor);
            cfList.add(cfItem);
        }

        // 异步阻塞，等待所有任务执行完成
        CompletableFuture.allOf(cfList.toArray(new CompletableFuture[0])).join();

        // 获取所有结果
        List<String> results = cfList.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        // 输出结果
        System.out.println("All results: " + results);
    }

}
