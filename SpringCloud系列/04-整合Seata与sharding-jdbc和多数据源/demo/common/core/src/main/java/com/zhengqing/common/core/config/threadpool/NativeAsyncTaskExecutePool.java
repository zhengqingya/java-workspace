package com.zhengqing.common.core.config.threadpool;

import com.zhengqing.common.base.constant.ThreadPoolConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p> 原生(Spring)异步任务线程池装配类 </p>
 *
 * @author zhengqingya
 * @description 配置默认的线程池，重写spring默认线程池的方式使用的时候，只需要加@Async注解就可以，不用去声明线程池类。
 * @date 2021/5/27 10:31
 */
@Slf4j
@EnableAsync
@Configuration
public class NativeAsyncTaskExecutePool implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程池大小
        threadPoolTaskExecutor.setCorePoolSize(5);
        // 最大线程数
        threadPoolTaskExecutor.setMaxPoolSize(10);
        // 队列容量
        threadPoolTaskExecutor.setQueueCapacity(200);
        // 活跃时间 60s
        threadPoolTaskExecutor.setKeepAliveSeconds(60);
        // 线程名字前缀
        threadPoolTaskExecutor.setThreadNamePrefix(ThreadPoolConstant.SPRING_DEFAULT_THREAD_NAME_PREFIX);
        // 设置在关闭线程池时是否等待任务完成
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 允许核心线程超时
        threadPoolTaskExecutor.setAllowCoreThreadTimeOut(true);
        // 等待时间 （默认为0，此时立即停止），并没等待xx秒后强制停止
        threadPoolTaskExecutor.setAwaitTerminationSeconds(60);
        // 修改拒绝策略为使用当前线程执行
        // setRejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务
        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化线程池
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    /**
     * 异步任务中异常处理 （注：只能捕获到@Async下无返回值的方法）
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
                log.error("exception method: 【{}】", method);
                log.error("exception msg: ", throwable);
            }
        };
    }
}
