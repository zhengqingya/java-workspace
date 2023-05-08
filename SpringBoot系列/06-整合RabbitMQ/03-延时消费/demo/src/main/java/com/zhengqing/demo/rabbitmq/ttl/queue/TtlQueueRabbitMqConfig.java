package com.zhengqing.demo.rabbitmq.ttl.queue;

import com.google.common.collect.Maps;
import com.zhengqing.demo.constant.MqConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class TtlQueueRabbitMqConfig {

    /**
     * 配置队列
     */
    @Bean
    public Queue ttlQueueQueue() {
        Map<String, Object> args = Maps.newHashMap();
        args.put("x-message-ttl", MqConstant.TTL_QUEUE_TIME);
        return new Queue(MqConstant.TTL_QUEUE_QUEUE, true, false, false, args);
    }

}
