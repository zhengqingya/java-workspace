package com.zhengqing.demo.rabbitmq.ttl.msg;

import com.zhengqing.demo.constant.MqConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TtlMsgRabbitMqConfig {

    /**
     * 配置交换机
     */
    @Bean
    public DirectExchange ttlMsgExchange() {
        return new DirectExchange(MqConstant.TTL_MSG_EXCHANGE, true, false);
    }

    /**
     * 配置队列
     */
    @Bean
    public Queue ttlMsgQueue() {
        return new Queue(MqConstant.TTL_MSG_QUEUE, true, false, false, null);
    }


    /**
     * 配置绑定
     */
    @Bean
    public Binding ttlMsgBinding() {
        return BindingBuilder.bind(ttlMsgQueue()).to(ttlMsgExchange()).with(MqConstant.TTL_MSG_ROUTING_KEY);
    }

}
