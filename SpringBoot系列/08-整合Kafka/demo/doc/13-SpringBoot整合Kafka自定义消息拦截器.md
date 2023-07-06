# SpringBoot整合Kafka发送复杂对象

#### 1、`application.yml` 修改配置

这里只贴出相关核心配置

```yml
spring:
  kafka:
    producer:
      properties:
        # 自定义消息拦截器 -- 生产者
        interceptor.classes: com.zhengqing.demo.config.CustomProducerInterceptor
    consumer:
      properties:
        # 自定义消息拦截器 -- 消费者
        interceptor.classes: com.zhengqing.demo.config.CustomConsumerInterceptor
```

#### 2、自定义消息拦截器 -- 生产者

```java
package com.zhengqing.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

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
```

#### 3、自定义消息拦截器 -- 消费者

```java
package com.zhengqing.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.*;

@Slf4j
public class CustomConsumerInterceptor implements ConsumerInterceptor<String, Object> {

    @Override
    public ConsumerRecords<String, Object> onConsume(ConsumerRecords<String, Object> consumerRecords) {
        // 在消息被消费之前调用 --> 可以对消息进行处理或者过滤
        List<ConsumerRecord<String, Object>> filterRecords = new ArrayList<>();
        Map<TopicPartition, List<ConsumerRecord<String, Object>>> newRecords = new HashMap<>();
        Set<TopicPartition> partitions = consumerRecords.partitions();
        for (TopicPartition tp : partitions) {
            List<ConsumerRecord<String, Object>> records = consumerRecords.records(tp);
            for (ConsumerRecord<String, Object> record : records) {
                // 这里可以对拿到的数据做处理或者过滤...
                Object value = record.value();
                filterRecords.add(record);
            }
            if (filterRecords.size() > 0) {
                newRecords.put(tp, filterRecords);
            }
        }
        return new ConsumerRecords<>(newRecords);
    }

    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> map) {
        // 在位移提交后调用 --> 可以执行位移提交后的操作
//        map.forEach((k, v) -> log.info("[{}] offset:{}", k, v));
    }

    @Override
    public void close() {
        // 拦截器关闭时调用 --> 可以进行资源的清理工作
    }

    @Override
    public void configure(Map<String, ?> map) {
        // 配置拦截器 --> 可以获取和设置拦截器的配置参数
    }
}
```
