package com.zhengqing.demo.rabbitmq.delay.plugins;

import com.google.common.collect.Maps;
import com.zhengqing.demo.constant.MqConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class DelayConfig {

    @Bean
    public CustomExchange orderDelayExchange() {
        Map<String, Object> args = Maps.newHashMap();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(MqConstant.PLUGIN_DELAY_EXCHANGE, "x-delayed-message", true, false, args);
    }

    @Bean
    public Queue orderDelayQueue() {
        return new Queue(MqConstant.PLUGIN_DELAY_QUEUE, true);
    }

    @Bean
    public Binding orderDelayBinding() {
        return BindingBuilder.bind(orderDelayQueue())
                .to(orderDelayExchange())
                .with(MqConstant.PLUGIN_DELAY_ROUTING_KEY)
                .noargs();
    }

}

