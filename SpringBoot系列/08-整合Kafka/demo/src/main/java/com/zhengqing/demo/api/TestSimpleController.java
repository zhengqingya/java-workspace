package com.zhengqing.demo.api;

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
public class TestSimpleController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public static final String KAFKA_TOPIC_SIMPLE = "simple";

    @PostMapping("simple")
    @ApiOperation("简单消息")
    public String simple(@RequestParam String msg) {
        this.kafkaTemplate.send(KAFKA_TOPIC_SIMPLE, msg);
        return "SUCCESS";
    }

    @KafkaListener(topics = KAFKA_TOPIC_SIMPLE)
    public void listen(String value) {
        log.info("消费者: " + value);
    }

}
