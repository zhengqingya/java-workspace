package com.zhengqing.demo.api;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import io.github.majusko.pulsar.annotation.PulsarConsumer;
import io.github.majusko.pulsar.producer.PulsarTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/10/5 2:36 下午
 */
@Slf4j
@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Api(tags = "测试api")
public class TestController {

    private final PulsarTemplate pulsarTemplate;

    @SneakyThrows()
    @GetMapping("producer_string")
    @ApiOperation("生产者-string")
    public String producer_string() {
        String data = RandomUtil.randomString(10);
        log.info("[生产者]: {}", data);
        this.pulsarTemplate.send("topic_string", data);
        return "OK";
    }

    @PulsarConsumer(topic = "topic_string", clazz = String.class)
    public void consumer_string(String data) {
        log.info("[消费者]: {}", data);
    }


    @SneakyThrows()
    @GetMapping("producer_obj")
    @ApiOperation("生产者-obj")
    public String producer_obj() {
        User user = User.builder().id(RandomUtil.randomLong()).name(RandomUtil.randomString(10)).build();
        log.info("[生产者]: {}", JSONUtil.toJsonStr(user));
        this.pulsarTemplate.send("topic_obj", user);
        return "OK";
    }

    @PulsarConsumer(topic = "topic_obj", clazz = User.class)
    public void consumer_obj(User user) {
        log.info("[消费者]: {}", JSONUtil.toJsonStr(user));
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long id;
        private String name;
    }

}
