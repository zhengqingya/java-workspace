package com.zhengqing.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

/**
 * <p> Kafka消息批量消费工厂配置 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/7/6 15:37
 */
@Configuration
public class KafkaConsumerBatchMsgConfig {

//    @Value("${spring.kafka.bootstrap-servers}")
//    private String bootstrapServers;
//
//    @Value("${spring.kafka.consumer.group-id}")
//    private String groupId;
//
//    @Value("${spring.kafka.consumer.max-poll-records}")
//    private Integer maxPollRecords;
//
//    @Bean
//    public ConsumerFactory<String, String> consumerFactory() {
//        Map<String, Object> configs = new HashMap<>();
//        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
//        configs.put(ConsumerConfig.GROUP_ID_CONFIG, this.groupId);
//        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        // 每次拉取消息的最大记录数。用于控制每次消费的批量大小。
//        configs.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, this.maxPollRecords);
//        return new DefaultKafkaConsumerFactory<>(configs);
//    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> batchFactory(ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        // 上面局部自定义的
//        factory.setConsumerFactory(consumerFactory());
        // yml配置的
        factory.setConsumerFactory(consumerFactory);
        // 设置为批量监听
        factory.setBatchListener(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
        return factory;
    }

}
