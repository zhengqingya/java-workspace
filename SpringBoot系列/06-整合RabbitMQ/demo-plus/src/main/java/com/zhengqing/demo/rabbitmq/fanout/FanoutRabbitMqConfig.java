package com.zhengqing.demo.rabbitmq.fanout;

import com.zhengqing.demo.constant.MqConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutRabbitMqConfig {

    /**
     * 配置交换器
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(MqConstant.FANOUT_EXCHANGE);
    }

    /**
     * 配置队列
     */
    @Bean
    public Queue fanoutQueue1() {
        return new Queue(MqConstant.FANOUT_QUEUE_1, true, false, false, null);
    }

    @Bean
    public Queue fanoutQueue2() {
        return new Queue(MqConstant.FANOUT_QUEUE_2, true, false, false, null);
    }

    /**
     * 配置绑定
     */
    @Bean
    public Binding fanoutBinding1(FanoutExchange fanoutExchange, Queue fanoutQueue1) {
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }

    @Bean
    public Binding fanoutBinding2(FanoutExchange fanoutExchange, Queue fanoutQueue2) {
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }

}
