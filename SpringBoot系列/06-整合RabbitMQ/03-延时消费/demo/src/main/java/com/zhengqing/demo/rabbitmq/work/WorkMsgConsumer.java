package com.zhengqing.demo.rabbitmq.work;

import com.zhengqing.demo.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WorkMsgConsumer {

    @RabbitListener(queues = MqConstant.WORK_QUEUE)
    public void listener1(String msg) {
        log.info("[消费者1] 接收消息: {}", msg);
    }

    @RabbitListener(queues = MqConstant.WORK_QUEUE)
    public void listener2(String msg) {
        log.info("[消费者2] 接收消息: {}", msg);
    }

}
