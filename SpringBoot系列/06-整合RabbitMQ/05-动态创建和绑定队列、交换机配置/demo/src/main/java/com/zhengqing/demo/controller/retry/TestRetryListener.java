package com.zhengqing.demo.controller.retry;

import com.zhengqing.demo.dynamic.retry.CustomRetryListener;
import com.zhengqing.demo.dynamic.retry.RetryFailConsumer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.stereotype.Component;

/**
 * <p> 自定义消息重试 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/7/10 15:59
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TestRetryListener implements CustomRetryListener {

    private final RabbitTemplate rabbitTemplate;

    @SneakyThrows
    @Override
    public <E extends Throwable, T> void onRetry(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        log.info("[消费者失败回调-第{}次] 接收消息: {} 异常原因：{}", context.getRetryCount(), this.getData(callback), throwable.getCause().getMessage());
    }

    @Override
    public <E extends Throwable, T> void lastRetry(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        String msgData = this.getData(callback);
        log.info("[消费者失败回调-lastRetry] 接收消息: {} 异常原因：{}", msgData, throwable.getCause().getMessage());
        this.rabbitTemplate.convertAndSend(RetryFailConsumer.RETRY_EXCHANGE, RetryFailConsumer.RETRY_FAILURE_KEY, msgData);
    }

}
