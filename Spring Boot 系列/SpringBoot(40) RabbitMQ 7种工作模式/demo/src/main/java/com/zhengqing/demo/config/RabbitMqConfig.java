package com.zhengqing.demo.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p> RabbitMQ配置类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/3/23 9:41
 */
@Configuration
public class RabbitMqConfig {
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
