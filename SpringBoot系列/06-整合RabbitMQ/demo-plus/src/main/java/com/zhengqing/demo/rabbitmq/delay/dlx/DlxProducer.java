package com.zhengqing.demo.rabbitmq.delay.dlx;

import cn.hutool.core.date.DateTime;
import com.zhengqing.demo.constant.MqConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DlxProducer {

    private final AmqpTemplate rabbitTemplate;

    public void send() {
        String msgContent = "Hello World " + DateTime.now();
        log.info("{} [生产者] 发送消息: {}", DateTime.now(), msgContent);
//        this.rabbitTemplate.convertAndSend(MqConstant.ORDER_EXCHANGE, MqConstant.ORDER_ROUTING_KEY, msgContent);
        // 单条消息设置过期时间，如果队列中也设置了过期时间，以两者的最小过期时间计算
//        Message message = MessageBuilder.withBody(msgContent.getBytes())
//                .setExpiration(String.valueOf(3000))
//                .build();
//        this.rabbitTemplate.convertAndSend(MqConstant.ORDER_EXCHANGE, MqConstant.ORDER_ROUTING_KEY, message);

        this.rabbitTemplate.convertAndSend(MqConstant.ORDER_EXCHANGE, MqConstant.ORDER_ROUTING_KEY, msgContent, message -> {
            message.getMessageProperties().setExpiration("3000");
            return message;
        });
    }

}
