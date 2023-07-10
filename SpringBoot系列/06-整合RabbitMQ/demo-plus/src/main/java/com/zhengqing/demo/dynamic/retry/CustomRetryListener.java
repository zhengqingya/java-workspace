package com.zhengqing.demo.dynamic.retry;


import lombok.SneakyThrows;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.amqp.core.Message;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.interceptor.MethodInvocationRetryCallback;

import java.nio.charset.StandardCharsets;

/**
 * <p> 自定义消息重试 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/7/10 15:59
 */
public interface CustomRetryListener {

    /**
     * 重试失败回调 -- 每次
     */
    <E extends Throwable, T> void onRetry(RetryContext context, RetryCallback<T, E> callback, Throwable throwable);


    /**
     * 重试失败回调 -- 最后一次
     */
    <E extends Throwable, T> void lastRetry(RetryContext context, RetryCallback<T, E> callback, Throwable throwable);

    @SneakyThrows
    default String getData(RetryCallback callback) {
        MethodInvocation invocation = ((MethodInvocationRetryCallback) callback).getInvocation();
        Message msgData = (Message) invocation.getArguments()[1];
        byte[] body = msgData.getBody();
        String msg = new String(body, StandardCharsets.UTF_8);
        return msg;
    }

}
