package com.zhengqing.demo.rabbitmq.delay.dlx;

import com.google.common.collect.Maps;
import com.zhengqing.demo.constant.MqConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class DlxConfig {

    // ======================== ↓↓↓↓↓↓ 死信队列 ↓↓↓↓↓↓ ========================

    /**
     * 死信交换机
     */
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(MqConstant.DLX_EXCHANGE, true, false);
    }

    /**
     * 死信队列
     */
    @Bean
    public Queue dlxQueue() {
        return new Queue(MqConstant.DLX_QUEUE, true, false, false);
    }

    /**
     * 绑定死信交换机和死信队列
     */
    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue())
                .to(dlxExchange())
                .with(MqConstant.DLX_ROUTING_KEY);
    }

    // ======================== ↓↓↓↓↓↓ 普通消费队列 ↓↓↓↓↓↓ ========================

    /**
     * 普通消费队列
     */
    @Bean
    public Queue orderQueue() {
        Map<String, Object> args = Maps.newHashMap();
        // 设置消息过期时间
        args.put("x-message-ttl", MqConstant.ORDER_QUEUE_TTL_TIME);
        // 设置死信交换机
        args.put("x-dead-letter-exchange", MqConstant.DLX_EXCHANGE);
        // 设置死信
        args.put("x-dead-letter-routing-key", MqConstant.DLX_ROUTING_KEY);
        return new Queue(MqConstant.ORDER_QUEUE, true, false, false, args);
    }

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(MqConstant.ORDER_EXCHANGE, true, false);
    }

    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue())
                .to(orderExchange())
                .with(MqConstant.ORDER_ROUTING_KEY);
    }


}

