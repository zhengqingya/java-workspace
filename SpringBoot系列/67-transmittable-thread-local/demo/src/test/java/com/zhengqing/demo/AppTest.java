package com.zhengqing.demo;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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


}
