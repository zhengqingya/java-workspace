package com.zhengqing.demo.config;

import com.zhengqing.demo.api.TestController;
import io.github.majusko.pulsar.producer.ProducerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p> Pulsar配置 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/10/5 2:36 下午
 */
@Configuration
public class PulsarConfig {

    @Bean
    public ProducerFactory producerFactory() {
        return new ProducerFactory()
                .addProducer("topic_string", String.class)
                .addProducer("topic_obj", TestController.User.class);
    }

}