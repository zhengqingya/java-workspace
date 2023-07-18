package com.zhengqing.demo;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> 重试测试 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/7/18 14:50
 */
@Slf4j
public class RetryTest {
    /**
     * 重试间隔时间ms,默认1000ms
     */
    private final long fixedPeriodTime = 1000L;
    /**
     * 最大重试次数,默认为3
     */
    private final int maxAttempts = 3;
    /**
     * 表示哪些异常需要重试,key表示异常的字节码,value为true表示需要重试
     */
    private final Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<Class<? extends Throwable>, Boolean>() {{
        this.put(IllegalArgumentException.class, true);
    }};


    @Test
    public void test() {
        // 构建重试模板实例
        RetryTemplate retryTemplate = new RetryTemplate();
        /**
         * 设置重试策略，重试次数
         *     NeverRetryPolicy： 只允许调用RetryCallback一次，不允许重试
         *     AlwaysRetryPolicy： 允许无限重试，直到成功，此方式逻辑不当会导致死循环
         *     SimpleRetryPolicy： 固定次数重试策略，默认重试最大次数为3次，RetryTemplate默认使用的策略
         *     TimeoutRetryPolicy： 超时时间重试策略，默认超时时间为1秒，在指定的超时时间内允许重试
         *     ExceptionClassifierRetryPolicy： 设置不同异常的重试策略，类似组合重试策略，区别在于这里只区分不同异常的重试
         *     CircuitBreakerRetryPolicy： 有熔断功能的重试策略，需设置3个参数openTimeout、resetTimeout和delegate
         *     CompositeRetryPolicy： 组合重试策略，有两种组合方式，乐观组合重试策略是指只要有一个策略允许即可以重试，悲观组合重试策略是指只要有一个策略不允许即可以重试，但不管哪种组合方式，组合中的每一个策略都会执行
         */
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(this.maxAttempts, this.retryableExceptions);
        retryTemplate.setRetryPolicy(retryPolicy);
        /**
         * 设置重试回退操作策略，主要设置重试间隔时间 （指的是每次重试是立即重试还是等待一段时间后重试。）
         * 默认情况下是立即重试，如果需要配置等待一段时间后重试则需要指定回退策略BackoffRetryPolicy。
         *     NoBackOffPolicy： 无退避算法策略，每次重试时立即重试
         *     FixedBackOffPolicy： 固定时间的退避策略，需设置参数sleeper和backOffPeriod，sleeper指定等待策略，默认是Thread.sleep，即线程休眠，backOffPeriod指定休眠时间，默认1秒
         *     UniformRandomBackOffPolicy： 随机时间退避策略，需设置sleeper、minBackOffPeriod和maxBackOffPeriod，该策略在minBackOffPeriod,maxBackOffPeriod之间取一个随机休眠时间，minBackOffPeriod默认500毫秒，maxBackOffPeriod默认1500毫秒
         *     ExponentialBackOffPolicy： 指数退避策略，需设置参数sleeper、initialInterval、maxInterval和multiplier，initialInterval指定初始休眠时间，默认100毫秒，maxInterval指定最大休眠时间，默认30秒，multiplier指定乘数，即下一次休眠时间为当前休眠时间*multiplier
         *     ExponentialRandomBackOffPolicy： 随机指数退避策略，引入随机乘数可以实现随机乘数回退
         */
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(this.fixedPeriodTime);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        Boolean execute = retryTemplate.execute(
                // RetryCallback 重试回调逻辑实例
                retryContext -> {
                    boolean b = this.retryTest(RandomUtil.randomString(5));
                    log.info("调用结果:{}", b);
                    return b;
                },
                // RecoveryCallback 整个执行操作结束的恢复操作实例
                retryContext -> {
                    log.info("已达到最大重试次数或抛出了不重试的异常...");
                    return false;
                }
        );
        log.info("执行结果: {}", execute);
    }

    /**
     * 重试方法
     */
    public boolean retryTest(String param) {
        log.info("参数:{}", param);
        throw new IllegalArgumentException("抛出异常...");
//        return true;
    }

}

