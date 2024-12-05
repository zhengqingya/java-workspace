package com.zhengqing.demo.api;

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.FixedDelayStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> kafka </p>
 *
 * @author zhengqing
 * @description
 * @date 2020/4/21 22:34
 */
@Slf4j
@RestController
@RequestMapping("/api/test/")
@RequiredArgsConstructor
@Api(tags = "测试API")
public class TestRetry1Controller {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public static final String KAFKA_TOPIC = "my-retry";

    @PostMapping("retry")
    @ApiOperation("消息重试")
    public String retry(@RequestParam String msg) {
        log.info("[生产者] 发送消息：{} 时间：{}", msg, DateUtil.now());
        this.kafkaTemplate.send(KAFKA_TOPIC, "test", msg);
        return "SUCCESS";
    }

    @RetryableTopic(
            attempts = "2",  // 重试次数，默认为3。
            backoff = @Backoff(
                    delay = 5000,// 重试消费延迟时间，单位为毫秒。
                    multiplier = 2  // 重试延迟时间系数，eg: attempts = 4， delay = 5000， multiplier = 2 ，则间隔时间依次为5s、10s、20s、40s，最大延迟时间受 maxDelay 限制。
            ),
            // 配置重试主题
            numPartitions = "3", //重试主题的分区数量
            autoCreateTopics = "true", // 自动创建重试主题和DLT（死信队列）主题
            retryTopicSuffix = "-retry",  // 重试主题的后缀为 -retry
            dltTopicSuffix = "-dlt", // DLT主题的后缀为 -dlt
            fixedDelayTopicStrategy = FixedDelayStrategy.SINGLE_TOPIC // 使用单个重试主题的策略
    )
    @KafkaListener(topics = KAFKA_TOPIC)
    public void listen(String value) {
        log.info("[消费者] 重试验证：{} 时间：{}", value, DateUtil.now());
        int a = 1 / 0;
        log.info("消费者: {}", value);
    }

    // 监听方式1
    @DltHandler
    public void dltHandler1(ConsumerRecord<String, String> record) {
        log.info("[DLT死信队列1] topic:{}, partition:{}, offset:{}, key:{}, value:{}",
                record.topic(), record.partition(), record.offset(), record.key(), record.value());
    }

    // 监听方式2
    @KafkaListener(
            topics = KAFKA_TOPIC + "-dlt",
//            topicPartitions = {@TopicPartition(topic = KAFKA_TOPIC + "-dlt", partitions = {"0", "1", "2"})},
            groupId = "my_group")
    public void dltHandler2(ConsumerRecord<String, String> record) {
        log.info("[DLT死信队列2] my_group topic:{}, partition:{}, offset:{}, key:{}, value:{}",
                record.topic(), record.partition(), record.offset(), record.key(), record.value());
    }

    @KafkaListener(
            topics = KAFKA_TOPIC + "-dlt",
//            topicPartitions = {@TopicPartition(topic = KAFKA_TOPIC + "-dlt", partitions = {"0", "1", "2"})},
            groupId = "my_group2")
    public void dltHandler3(ConsumerRecord<String, String> record) {
        log.info("[DLT死信队列2] my_group2 topic:{}, partition:{}, offset:{}, key:{}, value:{}",
                record.topic(), record.partition(), record.offset(), record.key(), record.value());
    }

}
