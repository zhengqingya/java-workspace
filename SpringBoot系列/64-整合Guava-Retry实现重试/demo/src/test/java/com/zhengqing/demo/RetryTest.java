package com.zhengqing.demo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * <p> 重试测试 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/7/18 14:50
 */
@Slf4j
public class RetryTest {

    @Test
    public void test() {
        // 构建重试实例 retryer，可以设置重试源且可以支持多个重试源，可以配置重试次数或重试超时时间，以及可以配置等待时间间隔
        Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
                /**
                 * 重试条件 retryIfException、retryIfExceptionOfType、retryIfRuntimeException、retryIfResult
                 */
                // 发生任何异常都重试
//                .retryIfException()
                // 匹配到指定类型异常时重试
//                .retryIfException(e -> e instanceof IllegalArgumentException)
                // 只会在抛 runtime 异常的时候才重试，checked 异常和error 都不重试。
//                .retryIfRuntimeException()
                // 允许我们只在发生特定异常的时候才重试，比如NullPointerException 和 IllegalStateException 都属于 runtime 异常，也包括自定义的error。
                .retryIfExceptionOfType(IllegalArgumentException.class)
                // 根据返回值结果重试 eg: 返回值等于false就进行重试
                .retryIfResult(res -> res == false)
                /**
                 * 重试等待策略，设置等待间隔时间
                 *    FixedWaitStrategy：固定等待时长策略，比如每次重试等待5s
                 *    RandomWaitStrategy：随机等待时长策略，每次重试等待指定区间的随机时长
                 *    IncrementingWaitStrategy：递增等待时长策略，指定初始等待值，然后重试间隔随次数等差递增，比如依次等待10s、30s、60s（递增值为10）
                 *    ExponentialWaitStrategy：指数等待时长策略，指定初始值，然后每次重试间隔乘2（即间隔为2的幂次方），如依次等待 2s、6s、14s。可以设置最大等待时长，达到最大值后每次重试将等待最大时长。
                 *    FibonacciWaitStrategy ：斐波那契等待时长策略，类似指数等待时长策略，间隔时长为斐波那契数列。
                 *    ExceptionWaitStrategy：异常时长等待策略，根据出现的异常类型决定等待的时长
                 *    CompositeWaitStrategy ：复合时长等待策略，可以组合多个等待策略，基本可以满足所有等待时长的需求
                 */
                .withWaitStrategy(WaitStrategies.fixedWait(3, TimeUnit.SECONDS))
                /**
                 * 停止策略，这里设置最大重试次数为3
                 *    NeverStopStrategy：永不停止，直到重试成功
                 *    StopAfterAttemptStrategy：指定最多重试次数，超过次数抛出 RetryException 异常
                 *    StopAfterDelayStrategy：指定最长重试时间，超时则中断当前任务执行且不再重试，并抛出 RetryException 异常
                 */
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                /**
                 * 阻塞策略，阻塞策略控制当前重试结束至下次重试开始前的行为
                 */
                .withBlockStrategy(new BlockStrategy() {
                    @Override
                    public void block(long l) throws InterruptedException {
                        Thread.sleep(l);
                    }
                })
                /**
                 * 超时限制，为任务添加单次执行时间限制，超时则中断执行，继续重试。
                 *    NoAttemptTimeLimit：不限制执行时间
                 *    FixedAttemptTimeLimit：限制执行时间为固定值
                 */
//                .withAttemptTimeLimiter()
                // 重试监听器
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        log.error("时间：{} 第{}次失败 原因：{}", DateUtil.now(), attempt.getAttemptNumber(), attempt.getExceptionCause().getMessage());
                    }
                })
                .build();
        try {
            // 执行任务
            retryer.call(() -> this.retryTest(RandomUtil.randomString(5)));
        } catch (Exception e) {
            log.error("异常：", e);
        }
    }

    /**
     * 重试测试
     */
    public boolean retryTest(String param) {
        log.info("参数:{}", param);
        throw new IllegalArgumentException("抛出异常...");
//        return false;
    }

}

