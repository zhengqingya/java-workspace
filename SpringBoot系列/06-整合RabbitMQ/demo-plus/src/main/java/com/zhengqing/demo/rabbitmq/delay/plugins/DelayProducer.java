package com.zhengqing.demo.rabbitmq.delay.plugins;

import cn.hutool.core.date.DateTime;
import com.zhengqing.demo.constant.MqConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DelayProducer {

    private final AmqpTemplate rabbitTemplate;

    public void send() {
        String msgContent = "Hello World " + DateTime.now();
        log.info("{} [生产者] 发送消息: {}", DateTime.now(), msgContent);
        this.rabbitTemplate.convertAndSend(MqConstant.PLUGIN_DELAY_EXCHANGE,
                MqConstant.PLUGIN_DELAY_ROUTING_KEY,
                msgContent, message -> {
                    // 配置消息延时时间-3秒
                    message.getMessageProperties().setHeader("x-delay", 1000 * 3);
                    return message;
                });
    }

}
