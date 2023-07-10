package com.zhengqing.demo.rabbitmq.rpc;

import com.zhengqing.demo.constant.MqConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RpcMsgProducer {

    private final AmqpTemplate rabbitTemplate;

    public void send() {
        String msgContent = "Hello World";
        log.info("[生产者] 发送消息: {}", msgContent);
        Object msgObj = this.rabbitTemplate.convertSendAndReceive(MqConstant.RPC_QUEUE, msgContent);
        log.info("[生产者] 接收回应：{}", msgObj);
    }

}
