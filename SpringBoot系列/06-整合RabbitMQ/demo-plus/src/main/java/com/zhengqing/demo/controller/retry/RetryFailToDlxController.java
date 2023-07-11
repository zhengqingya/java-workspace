package com.zhengqing.demo.controller.retry;

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
@RequestMapping("/api/mq/retry/dlx")
@RequiredArgsConstructor
public class RetryFailToDlxController {
    private final RabbitTemplate rabbitTemplate;

    @ApiOperation("消息重试失败转死信队列（由于动态队列导致失效）")
    @PostMapping("producer")
    public String producer() {
        String msgContent = "Hello World " + DateTime.now();
        log.info("{} [生产者] 发送消息: {}", DateTime.now(), msgContent);
        this.rabbitTemplate.convertAndSend("test_exchange", "test_routing_key_retry_to_dlx", msgContent);
        return "SUCCESS";
    }

    /**
     * 普通队列消费
     */
    @SneakyThrows
    @RabbitHandler
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "test_queue_retry_to_dlx", durable = "true"
                            , arguments = {
                            @Argument(name = "x-dead-letter-exchange", value = "dlx_exchange"),
                            @Argument(name = "x-message-ttl", value = "1800000", type = "java.lang.Long"),
                            @Argument(name = "x-dead-letter-routing-key", value = "test_routing_key_dlx")
                    }
                    ),
                    exchange = @Exchange(value = "test_exchange", type = "direct", durable = "true"),
                    key = "test_routing_key_retry_to_dlx"
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

    /**
     * 普通队列消息重试依然失败转入死信队列进行消息补偿机制
     */
    @RabbitListener(
            bindings = {
                    @QueueBinding(
                            value = @Queue(name = "test_queue_dlx", durable = "true"),
                            exchange = @Exchange(value = "dlx_exchange", type = "direct", durable = "true"),
                            key = "test_routing_key_dlx"
                    )
            }
    )
    public void dlx(String msg) throws Exception {
        log.info("[死信队列] 接收消息: {}", msg);
        // 如果死信队列异常 重试一轮之后完事... 不会无限轮重试
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            log.error("[死信队列] 异常:{}", e.getMessage());
            throw e;
        }
    }

}