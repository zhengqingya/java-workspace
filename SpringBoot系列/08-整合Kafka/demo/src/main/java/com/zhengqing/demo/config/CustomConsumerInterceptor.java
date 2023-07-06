package com.zhengqing.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.*;

/**
 * <p> 自定义消费者拦截器 -- 消费者 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/7/6 15:37
 */
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