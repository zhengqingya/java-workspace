package com.zhengqing.demo.modules.rabbitmq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * <p> rabbitmq配置文件 </p>
 *
 * @author : zhengqing
 * @description : 配置Queue(消息队列)
 * @date : 2019/12/31 17:48
 */
@Configuration
public class RabbitConfig {

    public static final String QUEUE_KEY = "hello_world";

    @Bean
    public Queue queue() {
        // durable: true 标识开启消息队列持久化 (队列当中的消息在重启rabbitmq服务的时候还会存在)
        return new Queue(QUEUE_KEY, true);
    }

}
