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
public class TestRetryController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public static final String KAFKA_TOPIC = "my-retry";

    @PostMapping("retry")
    @ApiOperation("消息重试")
    public String retry(@RequestParam String msg) {
        this.kafkaTemplate.send(KAFKA_TOPIC, msg);
        return "SUCCESS";
    }

    @RetryableTopic(
            attempts = "4",  // attempts：重试次数，默认为3。
            backoff = @Backoff(
                    delay = 5000,// 消费延迟时间，单位为毫秒。
                    multiplier = 2  // 延迟时间系数，此例中 attempts = 4， delay = 5000， multiplier = 2 ，则间隔时间依次为5s、10s、20s、40s，最大延迟时间受 maxDelay 限制。
            ),
            fixedDelayTopicStrategy = FixedDelayStrategy.SINGLE_TOPIC // 可选策略包括：SINGLE_TOPIC 、MULTIPLE_TOPICS
    )
    @KafkaListener(topics = KAFKA_TOPIC)
    public void listen(String value) {
        log.info("[消费者] 重试验证：{} 时间：{}", value, DateUtil.now());
        int a = 1 / 0;
        log.info("消费者: {}", value);
    }

    @DltHandler
    public void dltHandler(ConsumerRecord<String, String> record) {
        log.info("[DLT死信队列] topic:{}, key:{}, value:{}", record.topic(), record.key(), record.value());
    }

}
