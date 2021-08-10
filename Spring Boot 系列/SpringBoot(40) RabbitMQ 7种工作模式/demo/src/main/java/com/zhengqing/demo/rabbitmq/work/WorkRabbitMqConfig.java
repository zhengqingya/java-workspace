package com.zhengqing.demo.rabbitmq.work;

import com.zhengqing.demo.constant.MqConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkRabbitMqConfig {

    @Bean
    public Queue workQueue() {
        // durable: true 标识开启消息队列持久化 (队列当中的消息在重启rabbitmq服务的时候还会存在)
        return new Queue(MqConstant.WORK_QUEUE, true);
    }

}
