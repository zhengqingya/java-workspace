package com.zhengqing.demo.config;

import cn.hutool.json.JSONUtil;
import com.zhengqing.demo.dynamic.RabbitModuleInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
    @Bean
    @ConditionalOnMissingBean
    public RabbitModuleInitializer rabbitModuleInitializer(AmqpAdmin amqpAdmin, RabbitModuleProperty rabbitModuleProperty) {
        return new RabbitModuleInitializer(amqpAdmin, rabbitModuleProperty);
    }

    /**
     * 消息发送回调
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        // 设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        // 确认消息送到交换机(Exchange)回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            // do your business
            log.debug("\n[确认消息送到交换机(Exchange)回调] 是否成功:[{}] 数据：[{}] 异常：[{}]", ack, JSONUtil.toJsonStr(correlationData), cause);
        });

        // 确认消息送到队列(Queue)回调 -- 只有在出现错误时才回调
        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            // do your business
            log.error("\n[确认消息送到队列(Queue)回调] 返回信息：[{}]", JSONUtil.toJsonStr(returnedMessage));
        });

        return rabbitTemplate;
    }

}
