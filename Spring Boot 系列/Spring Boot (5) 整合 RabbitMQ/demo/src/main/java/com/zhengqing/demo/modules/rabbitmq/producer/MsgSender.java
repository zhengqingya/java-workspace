package com.zhengqing.demo.modules.rabbitmq.producer;

import com.zhengqing.demo.modules.rabbitmq.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p> 生产者 - 发送消息 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/2 11:27
 */
@Slf4j
@Component
public class MsgSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send() {
        String msgContent = "Hello World ~";
        log.info("生产者发送消息 : " + msgContent);
        this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_KEY, msgContent);
    }

}
