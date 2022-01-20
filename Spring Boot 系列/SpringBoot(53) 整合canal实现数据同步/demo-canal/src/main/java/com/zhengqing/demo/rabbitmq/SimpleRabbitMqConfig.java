package com.zhengqing.demo.rabbitmq;

import com.zhengqing.demo.constant.MqConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimpleRabbitMqConfig {

    @Bean
    public Queue simpleQueue() {
        // durable: true 标识开启消息队列持久化 (队列当中的消息在重启rabbitmq服务的时候还会存在)
        return new Queue(MqConstant.SIMPLE_QUEUE, true);
    }

}
