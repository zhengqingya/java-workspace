package com.zhengqing.demo.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

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
public class TestObjectController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public static final String KAFKA_TOPIC_OBJECT = "object";

    @PostMapping("object")
    @ApiOperation("对象消息")
    public String object(@RequestBody User user) {
        this.kafkaTemplate.send(KAFKA_TOPIC_OBJECT, user);
        return "SUCCESS";
    }

    @KafkaListener(topics = KAFKA_TOPIC_OBJECT)
    public void listen(User value) {
        log.info("消费者: " + value);
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    static class User implements Serializable {
        private static final long serialVersionUID = 1L;
        @ApiModelProperty(example = "666")
        private String id;
        @ApiModelProperty(example = "zhengqingya")
        private String name;
    }
}
