package com.zhengqing.demo.rabbitmq.topic;

import com.zhengqing.demo.constant.MqConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicRabbitMqConfig {

    /**
     * 配置交换器
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(MqConstant.TOPIC_EXCHANGE);
    }

    /**
     * 配置队列
     */
    @Bean
    public Queue topicQueue1() {
        return new Queue(MqConstant.TOPIC_QUEUE_1);
    }

    @Bean
    public Queue topicQueue2() {
        return new Queue(MqConstant.TOPIC_QUEUE_2);
    }

    /**
     * 配置绑定
     */
    @Bean
    public Binding topicBinding1(Queue topicQueue1, TopicExchange topicExchange) {
        // *：只能匹配一个词
        return BindingBuilder.bind(topicQueue1).to(topicExchange).with("topic.*");
    }

    @Bean
    public Binding topicBinding2(Queue topicQueue2, TopicExchange topicExchange) {
        // #：可匹配多个词
        return BindingBuilder.bind(topicQueue2).to(topicExchange).with("topic.#");
    }

}
