package com.zhengqing.demo.controller;

import cn.hutool.core.date.DateTime;
import com.rabbitmq.client.Channel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
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
        this.rabbitTemplate.convertAndSend("test_exchange", "test_routing_key_retry", msgContent);
        return "SUCCESS";
    }

    @SneakyThrows
//    @RabbitHandler
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "test_queue_retry", durable = "true"),
                    exchange = @Exchange(value = "test_exchange", type = "direct", durable = "true"),
                    key = "test_routing_key_retry"
            )
    )
    public void consumer(String msg) {
        log.info("{} [消费者] 接收消息: {}", DateTime.now(), msg);
//        int a = 1 / 0;
        throw new Exception("异常了...");
    }

    @RabbitListener(
            bindings = {
                    @QueueBinding(value = @Queue(
                            name = "test_queue_dlx",
                            durable = "true",
                            arguments = {
                                    @Argument(name = "x-dead-letter-exchange", value = "dlx_exchange"),
                                    @Argument(name = "x-message-ttl", value = "5000", type = "java.lang.Long"),
                                    @Argument(name = "x-dead-letter-routing-key", value = "test_routing_key_dlx")
                            }),
                            exchange = @Exchange(value = "test_exchange", type = "direct", durable = "true")
                    )
            }
    )
    public void dlx(Message message, Channel channel) throws Exception {
        log.info("[死信队列] 接收消息: {}", DateTime.now(), new String(message.getBody(), "UTF-8"));
    }

}
