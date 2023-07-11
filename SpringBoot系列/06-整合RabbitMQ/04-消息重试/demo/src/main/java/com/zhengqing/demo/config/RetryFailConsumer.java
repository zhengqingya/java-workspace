package com.zhengqing.demo.config;

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

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitMqConfig.RETRY_FAILURE_QUEUE, durable = "true"),
                    exchange = @Exchange(value = RabbitMqConfig.RETRY_EXCHANGE, type = "direct", durable = "true"),
                    key = RabbitMqConfig.RETRY_FAILURE_KEY
            )
    )
    public void retryFailConsumer(Message message, Channel channel) throws Exception {
        log.info("[消息重试失败] 接收时间: {} 接收消息: {}", DateTime.now(), new String(message.getBody(), StandardCharsets.UTF_8));
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            log.error("[消息重试失败] 异常:{}", e.getMessage());
            // 如果这里再抛出异常则继续走消息重试...
//            throw e;
        }
    }

}
