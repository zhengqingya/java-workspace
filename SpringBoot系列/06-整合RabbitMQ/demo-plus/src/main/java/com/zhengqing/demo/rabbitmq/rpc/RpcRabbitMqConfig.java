package com.zhengqing.demo.rabbitmq.rpc;

import com.zhengqing.demo.constant.MqConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RpcRabbitMqConfig {

    @Bean
    public Queue rpcQueue() {
        return new Queue(MqConstant.RPC_QUEUE);
    }

}
