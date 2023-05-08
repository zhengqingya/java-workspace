package com.zhengqing.demo.rabbitmq.ttl.msg;

import cn.hutool.core.thread.ThreadUtil;
import com.zhengqing.demo.constant.MqConstant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TtlMsgConsumer {

    /**
     * @RabbitListener 具有监听指定队列、指定exchange、指定routingKey的消息和建立队列、exchange、routingKey的功能
     */
    @SneakyThrows
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = MqConstant.TTL_MSG_QUEUE, durable = "true"),
                    exchange = @Exchange(value = MqConstant.TTL_MSG_EXCHANGE, type = "direct", durable = "true"),
                    key = MqConstant.TTL_MSG_ROUTING_KEY
            ))
//    @RabbitListener(queues = MqConstant.TTL_MSG_QUEUE)
    public void listener(String msg) {
        log.info("[消费者] 接收消息: {}", msg);
        ThreadUtil.sleep(5, TimeUnit.SECONDS);
        throw new Exception("fd");
    }

}
