package com.zhengqing.demo.config;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.config.AbstractRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.AbstractRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p> RabbitMQ配置类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/7/8 10:32
 */
@Slf4j
@Configuration
public class RabbitMqConfig {

    /**
     * 动态创建队列、交换机初始化器
     */
//    @Bean
//    @ConditionalOnMissingBean
//    public RabbitMqDynamicInitializer rabbitMqDynamicInitializer(ConnectionFactory connectionFactory, AmqpAdmin amqpAdmin, RabbitModulePropertys rabbitModulePropertys, RabbitTemplate rabbitTemplate) {
//        return new RabbitMqDynamicInitializer(connectionFactory, amqpAdmin, rabbitModulePropertys, rabbitTemplate);
//    }

    /**
     * 生产者配置
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);

        /**
         * 1、消息发送回调
         */
        // 设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        // 确认消息送到交换机(Exchange)回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            // do your business
            log.debug("[确认消息送到交换机(Exchange)回调] 是否成功:[{}] 数据：[{}] 异常：[{}]", ack, JSONUtil.toJsonStr(correlationData), cause);
        });

        // 确认消息送到队列(Queue)回调 -- 只有在出现错误时才回调，延时队列也会触发！
        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            // do your business
            log.error("[确认消息送到队列(Queue)回调] 返回信息：[{}]", JSONUtil.toJsonStr(returnedMessage));
        });

        /**
         * 2、配置自定义消息转换器
         * rabbitmq默认的消息转换器 {@link org.springframework.amqp.support.converter.SimpleMessageConverter}
         */
        rabbitTemplate.setMessageConverter(new CustomMessageConverter());
        // json消息转换器
//        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

        return rabbitTemplate;
    }


    /**
     * 消费者配置
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        /**
         * 配置自定义消息转换器
         * rabbitmq默认的消息转换器 {@link org.springframework.amqp.support.converter.SimpleMessageConverter}
         */
        factory.setMessageConverter(new CustomMessageConverter());
        // json消息转换器
//        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

        return factory;
    }

//    @Bean
//    public Jackson2JsonMessageConverter jsonMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }

    /**
     * tips: 使用动态队列后好像会失效，因此改用自定义消息重试，再转发队列补偿...
     * 修改消息失败策略
     * 默认配置： {@link AbstractRabbitListenerContainerFactoryConfigurer#configure(AbstractRabbitListenerContainerFactory, ConnectionFactory, RabbitProperties.AmqpContainer)}
     * MessageRecoverer recoverer = this.messageRecoverer != null ? this.messageRecoverer : new RejectAndDontRequeueRecoverer(); 默认拒绝&不重新排队
     */
//    @Bean
//    public MessageRecoverer republishMessageRecoverer(RabbitTemplate rabbitTemplate) {
//        /**
//         return new RejectAndDontRequeueRecoverer(); // 拒绝&不重新排队(默认)
//         return new MessageBatchRecoverer() {public void recover(List<Message> messages, Throwable cause) {}}; // 用于消息批量处理的恢复器（Recoverer），它可以在消息消费失败时对一个批量的消息进行统一的处理。
//         return new ImmediateRequeueMessageRecoverer(); // 重新排队 -- 重试之后，返回队列，然后再重试，周而复始直到不抛出异常为止，这样还是会影响后续的消息消费...
//         return new RepublishMessageRecoverer(rabbitTemplate, RETRY_EXCHANGE, RETRY_FAILURE_KEY); // 重新发布 -- 重试之后，将消息转发到重试失败队列，由重试失败消费者消费...
//         */
//        return new RepublishMessageRecoverer(rabbitTemplate, RETRY_EXCHANGE, RETRY_FAILURE_KEY);
//    }

}
