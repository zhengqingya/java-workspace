package com.zhengqing.demo.api;

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
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
public class TestRetry2Controller {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public static final String KAFKA_TOPIC = "my-retry-2";

    /**
     * 默认重试10次，无重试时间间隔...
     * 可观察控制台日志：Backoff FixedBackOff{interval=0, currentAttempts=10, maxAttempts=9} exhausted for my-retry-2-1@2
     */
    @PostMapping("retry2")
    @ApiOperation("消息重试-测试2")
    public String retry(@RequestParam String msg) {
        log.info("[生产者] 发送消息：{} 时间：{}", msg, DateUtil.now());
        this.kafkaTemplate.send(KAFKA_TOPIC, msg);
        return "SUCCESS";
    }

    @KafkaListener(topics = KAFKA_TOPIC)
    public void listen(String value) {
        log.info("[消费者] 重试验证：{} 时间：{}", value, DateUtil.now());
        int a = 1 / 0;
        log.info("消费者: {}", value);
    }

}
