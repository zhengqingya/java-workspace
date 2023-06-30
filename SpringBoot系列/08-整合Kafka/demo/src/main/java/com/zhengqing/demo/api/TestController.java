package com.zhengqing.demo.api;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api/")
@RequiredArgsConstructor
public class TestController {

    private final KafkaTemplate kafkaTemplate;

    public static final String KAFKA_TOPIC_SIMPLE = "simple";

    @GetMapping("simple")
    public String simple(@RequestParam String msg) {
        this.kafkaTemplate.send(KAFKA_TOPIC_SIMPLE, JSONUtil.toJsonStr(User.builder().id(IdUtil.fastUUID()).name(msg).build()));
        return "SUCCESS";
    }

    @KafkaListener(topics = KAFKA_TOPIC_SIMPLE)
    public void listen(String value) {
        log.info("消费者: " + value);
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    static class User {
        private String id;
        private String name;
    }
}
