package com.zhengqing.demo.rabbitmq.direct;

import com.zhengqing.demo.constant.MqConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectRabbitMqConfig {

    /**
     * 配置交换机
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(MqConstant.DIRECT_EXCHANGE);
    }

    /**
     * 配置队列
     */
    @Bean
    public Queue directQueue1() {
        return new Queue(MqConstant.DIRECT_QUEUE_1, true, false, false, null);
    }

    @Bean
    public Queue directQueue2() {
        return new Queue(MqConstant.DIRECT_QUEUE_2, true, false, false, null);
    }

    /**
     * 配置绑定
     */
    @Bean
    public Binding directBinding1(Queue directQueue1, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueue1).to(directExchange).with("one");
    }

    @Bean
    public Binding directBinding2(Queue directQueue2, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueue2).to(directExchange).with("two");
    }

}
