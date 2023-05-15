package com.zhengqing.demo.rabbitmq.direct;

import com.zhengqing.demo.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DirectMsgConsumer {

    /**
     * @RabbitListener 具有监听指定队列、指定exchange、指定routingKey的消息
     * 和建立队列、exchange、routingKey的功能
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = MqConstant.DIRECT_QUEUE_1, durable = "true"),
                    exchange = @Exchange(value = MqConstant.DIRECT_EXCHANGE, type = "direct", durable = "true"),
                    key = "one"
            ))
//    @RabbitListener(queues = MqConstant.DIRECT_QUEUE_1)
    public void listener1(String msg) {
        log.info("[消费者1] 接收消息: {}", msg);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = MqConstant.DIRECT_QUEUE_2, durable = "true"),
                    exchange = @Exchange(value = MqConstant.DIRECT_EXCHANGE, type = "direct", durable = "true"),
                    key = "two"
            ))
    //    @RabbitListener(queues = MqConstant.DIRECT_QUEUE_2)
    public void listener2(String msg) {
        log.info("[消费者2] 接收消息: {}", msg);
    }

}
