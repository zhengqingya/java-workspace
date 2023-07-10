package com.zhengqing.demo.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@Slf4j
@Api(tags = "测试mq")
@RestController
@RequestMapping("/api/mq")
@RequiredArgsConstructor
public class TestController {
    private final RabbitTemplate rabbitTemplate;

    @ApiOperation("发送消息")
    @PostMapping("send")
    public String producer() {
        this.rabbitTemplate.convertAndSend("test_exchange", "test_routing_key",
                UserDTO.builder()
                        .userId(IdUtil.getSnowflakeNextId())
                        .username("Hello World " + DateTime.now())
                        .build()
        );
        return "SUCCESS";
    }

    @SneakyThrows
    @RabbitHandler
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "test_queue", durable = "true"),
                    exchange = @Exchange(value = "test_exchange", type = "direct", durable = "true"),
                    key = "test_routing_key"
            )
    )
    public void consumer(UserDTO data) {
        log.info("{} [消费者] 接收消息: {}", DateTime.now(), JSONUtil.toJsonStr(data));
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private Long userId;
        private Integer age;
        private String username;
    }

}
