package com.zhengqing.demo.rabbitmq.delay.plugins;

import cn.hutool.core.date.DateTime;
import com.zhengqing.demo.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class DelayListener {

    @RabbitHandler
    @RabbitListener(queues = MqConstant.PLUGIN_DELAY_QUEUE)
    public void delayMsg(String msg) {
        log.info("----------------------------------------------");
        log.info("{} [消费者] 接收消息: {}", DateTime.now(), msg);
    }

}

