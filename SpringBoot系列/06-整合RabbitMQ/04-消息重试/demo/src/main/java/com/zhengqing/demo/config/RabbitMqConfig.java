package com.zhengqing.demo.config;

import org.springframework.amqp.rabbit.config.AbstractRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.boot.autoconfigure.amqp.AbstractRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p> RabbitMQ配置类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/7/8 10:32
 */
@Configuration
public class RabbitMqConfig {

    public static final String RETRY_EXCHANGE = "retry_exchange";
    public static final String RETRY_FAILURE_KEY = "retry_fail_routing_key";
    public static final String RETRY_FAILURE_QUEUE = "retry_fail_queue";

    /**
     * 修改消息失败策略
     * 默认配置： {@link AbstractRabbitListenerContainerFactoryConfigurer#configure(AbstractRabbitListenerContainerFactory, ConnectionFactory, RabbitProperties.AmqpContainer)}
     * MessageRecoverer recoverer = this.messageRecoverer != null ? this.messageRecoverer : new RejectAndDontRequeueRecoverer(); 默认拒绝&不重新排队
     */
//    @Bean
    public MessageRecoverer messageRecoverer(RabbitTemplate rabbitTemplate) {
        /**
         return new RejectAndDontRequeueRecoverer(); // 拒绝&不重新排队(默认)
         return new MessageBatchRecoverer() {public void recover(List<Message> messages, Throwable cause) {}}; // 用于消息批量处理的恢复器（Recoverer），它可以在消息消费失败时对一个批量的消息进行统一的处理。
         return new ImmediateRequeueMessageRecoverer(); // 重新排队 -- 重试之后，返回队列，然后再重试，周而复始直到不抛出异常为止，这样还是会影响后续的消息消费...
         return new RepublishMessageRecoverer(rabbitTemplate, RETRY_EXCHANGE, RETRY_FAILURE_KEY); // 重新发布 -- 重试之后，将消息转发到重试失败队列，由重试失败消费者消费...
         */
        return new RepublishMessageRecoverer(rabbitTemplate, RETRY_EXCHANGE, RETRY_FAILURE_KEY);
    }

}
