package com.zhengqing.demo.rabbitmq.ttl.msg;

import com.zhengqing.demo.constant.MqConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TtlMsgProducer {

    private final AmqpTemplate rabbitTemplate;

    public void send() {
        String msgContent = "Hello World";
        log.info("[生产者] 发送消息: {}", msgContent);
        // 单条消息设置过期时间5秒钟，即在5秒内此消息未被消费成功则自动删除
        Message message = MessageBuilder.withBody(msgContent.getBytes())
                .setExpiration(MqConstant.TTL_MSG_TIME)
                .build();
        this.rabbitTemplate.convertAndSend(MqConstant.TTL_MSG_QUEUE, message);
    }

}
