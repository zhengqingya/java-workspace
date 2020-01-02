package com.zhengqing.demo.modules.rabbitmq.consumer;

import com.zhengqing.demo.modules.rabbitmq.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * <p> 消费者 - 接收消息 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/2 11:28
 */
@Slf4j
@Component
@RabbitListener(queues = RabbitConfig.QUEUE_KEY)
public class MsgReceiver {

    @RabbitHandler
    public void process(String msg) {
        log.info("消费者接收消息 : " + msg);
    }

}
