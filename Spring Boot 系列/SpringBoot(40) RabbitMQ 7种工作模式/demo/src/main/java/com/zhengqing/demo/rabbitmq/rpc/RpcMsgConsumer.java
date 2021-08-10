package com.zhengqing.demo.rabbitmq.rpc;

import com.zhengqing.demo.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RpcMsgConsumer {

    @RabbitListener(queues = MqConstant.RPC_QUEUE)
    public String listener(String msg) {
        log.info("[消费者] 接收消息: {}", msg);
        return "消费者back";
    }

}
