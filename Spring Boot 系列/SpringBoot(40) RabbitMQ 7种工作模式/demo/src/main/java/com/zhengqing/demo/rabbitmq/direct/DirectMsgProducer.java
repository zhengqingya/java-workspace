package com.zhengqing.demo.rabbitmq.direct;

import com.zhengqing.demo.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DirectMsgProducer {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send1() {
        String msgContent = "Hello World";
        log.info("[生产者1] 发送消息: {}", msgContent);
        this.rabbitTemplate.convertAndSend(MqConstant.DIRECT_EXCHANGE, "one", msgContent);
    }

    public void send2() {
        String msgContent = "Hello World";
        log.info("[生产者2] 发送消息: {}", msgContent);
        this.rabbitTemplate.convertAndSend(MqConstant.DIRECT_EXCHANGE, "two", msgContent);
    }

}
