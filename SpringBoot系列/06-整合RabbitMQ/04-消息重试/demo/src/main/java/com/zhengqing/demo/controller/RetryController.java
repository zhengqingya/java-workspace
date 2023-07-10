package com.zhengqing.demo.controller;

import cn.hutool.core.date.DateTime;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "测试mq-消息重试")
@RestController
@RequestMapping("/api/mq/retry")
@RequiredArgsConstructor
public class RetryController {
    private final RabbitTemplate rabbitTemplate;

    @ApiOperation("消息重试")
    @PostMapping("producer")
    public String producer() {
        String msgContent = "Hello World " + DateTime.now();
        log.info("{} [生产者] 发送消息: {}", DateTime.now(), msgContent);
        this.rabbitTemplate.convertAndSend("test_exchange", "test_routing_key_retry", msgContent);
        return "SUCCESS";
    }

    @SneakyThrows
    @RabbitHandler
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "test_queue_retry", durable = "true"),
                    exchange = @Exchange(value = "test_exchange", type = "direct", durable = "true"),
                    key = "test_routing_key_retry"
            )
    )
    public void consumer(String msg) {
        log.info("{} [消费者] 接收消息: {}", DateTime.now(), msg);
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            log.error("[消费者] 异常:{}", e.getMessage());
            throw e;
        }
    }

}
