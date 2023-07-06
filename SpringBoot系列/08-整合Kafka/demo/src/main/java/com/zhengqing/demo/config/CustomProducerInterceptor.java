package com.zhengqing.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * <p> 自定义消费者拦截器 -- 生产者 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/7/6 15:38
 */
@Slf4j
public class CustomProducerInterceptor implements ProducerInterceptor<String, Object> {

    @Override
    public ProducerRecord<String, Object> onSend(ProducerRecord<String, Object> producerRecord) {
        // 在消息发送到Kafka之前被调用，可以对消息进行修改或者增加附加信息。
        Object value = producerRecord.value();
        return new ProducerRecord<>(producerRecord.topic(), producerRecord.partition(), producerRecord.timestamp(), producerRecord.key(), value, producerRecord.headers());
    }

    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
        // 当消息被确认时被调用，可以根据确认结果执行相应的操作。
    }

    @Override
    public void close() {
        // 在拦截器关闭时被调用，用于资源的清理工作。
    }

    @Override
    public void configure(Map<String, ?> map) {
        // 配置拦截器 --> 可以获取和设置拦截器的配置参数
    }
}