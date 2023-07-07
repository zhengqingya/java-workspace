package com.zhengqing.demo.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;

/**
 * <p> Kafka消息重试配置 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/7/7 14:59
 */
@Configuration
public class KafkaRetryConfig {
    @Bean
    public RetryTopicConfiguration retryTopic(KafkaTemplate<String, String> template) {
        return RetryTopicConfigurationBuilder
                .newInstance()
                .maxAttempts(4)
                .fixedBackOff(5000)
                .includeTopics(Lists.newArrayList(
                        "my-retry"
                ))
                .create(template);
    }
}
