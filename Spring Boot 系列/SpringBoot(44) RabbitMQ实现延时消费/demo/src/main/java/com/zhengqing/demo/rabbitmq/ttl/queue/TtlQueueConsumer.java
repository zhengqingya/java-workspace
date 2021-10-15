package com.zhengqing.demo.rabbitmq.ttl.queue;

import cn.hutool.core.thread.ThreadUtil;
import com.zhengqing.demo.constant.MqConstant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.concurrent.TimeUnit;

@Slf4j
//@Component
public class TtlQueueConsumer {

    @SneakyThrows
    @RabbitListener(queues = MqConstant.TTL_QUEUE_QUEUE)
    public void listener(String msg) {
        log.info("[消费者] 接收消息: {}", msg);
        ThreadUtil.sleep(10, TimeUnit.SECONDS);
        throw new Exception("fd");
    }

}
