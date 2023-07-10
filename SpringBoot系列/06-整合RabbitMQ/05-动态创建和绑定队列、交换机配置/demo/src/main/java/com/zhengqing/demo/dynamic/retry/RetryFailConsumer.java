package com.zhengqing.demo.dynamic.retry;

import cn.hutool.core.date.DateTime;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * <p> 消息重试失败 -- 业务补偿机制 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/7/8 10:32
 */
@Slf4j
@Component
public class RetryFailConsumer {

    /**
     * 消息重试失败 -- 重新发布
     */
    public static final String RETRY_EXCHANGE = "retry_exchange";
    public static final String RETRY_FAILURE_KEY = "retry_fail_routing_key";
    public static final String RETRY_FAILURE_QUEUE = "retry_fail_queue";

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RETRY_FAILURE_QUEUE, durable = "true"),
                    exchange = @Exchange(value = RETRY_EXCHANGE, type = "direct", durable = "true"),
                    key = RETRY_FAILURE_KEY
            )
    )
    public void retryFailConsumer(Message message, Channel channel) throws Exception {
        log.info("[消息重试失败] 接收时间: {} 接收消息: {}", DateTime.now(), new String(message.getBody(), StandardCharsets.UTF_8));
    }

}
