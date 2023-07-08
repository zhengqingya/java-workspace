package com.zhengqing.demo.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.RandomUtil;
import com.rabbitmq.client.Channel;
import com.zhengqing.demo.util.MqUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Slf4j
@Api(tags = "其它测试")
@RestController
@RequestMapping("/api/mq/other")
@RequiredArgsConstructor
public class TestOtherController {

    private final AmqpTemplate rabbitTemplate;

    @ApiOperation("发送延时消息（插件方式）")
    @PostMapping("sendDelayMsg")
    public String sendDelayMsg() {
        String msgContent = "Hello World " + DateTime.now();
        log.info("{} [生产者] 发送消息: {}", DateTime.now(), msgContent);
        this.rabbitTemplate.convertAndSend("demo.delay.exchange",
                "demo.test_delay.routing.key",
                msgContent, message -> {
                    // 配置消息延时时间-3秒
                    message.getMessageProperties().setHeader("x-delay", 1000 * 3);
                    return message;
                });
        return "SUCCESS";
    }

    @SneakyThrows
    @RabbitHandler
    @RabbitListener(queues = "demo.test_delay.queue")
    public void delayMsg(String msg) {
        log.info("----------------------------------------------");
        log.info("{} [消费者] 接收消息: {}", DateTime.now(), msg);
    }

    // ----------------------------------------------------------------------------------

    @ApiOperation("发送消息并确认")
    @PostMapping("sendWithConfirm")
    public String sendWithConfirm() {
        String msgContent = "Hello World " + DateTime.now();
        MqUtil.send("test_exchange", "test_ack_sender_routing_key", msgContent);
        return "SUCCESS";
    }

    @SneakyThrows
    @RabbitHandler
    @RabbitListener(queues = "test_ack_sender_queue")
    public void sendWithConfirm(String msg) {
        log.info("{} [消费者] 接收消息: {}", DateTime.now(), msg);
    }

    // ----------------------------------------------------------------------------------

    // ----------------------------------------------------------------------------------

    @ApiOperation("测试顺序消费")
    @PostMapping("sort")
    public String sort() {
        String msgContent = "Hello World " + DateTime.now() + "\n" + RandomUtil.randomNumber();
        MqUtil.send("test_exchange", "test_sort_routing_key", msgContent);
        return "SUCCESS";
    }

    @SneakyThrows
    @RabbitHandler
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "test_sort_queue", durable = "true"),
                    exchange = @Exchange(value = "test_exchange", type = "direct", durable = "true"),
                    key = "test_sort_routing_key"
            ),
            concurrency = "1-1", // 最小1个，最大1个consumer
            containerFactory = "mqConsumerListenerContainer", // 指定队列配置
            ackMode = "MANUAL"// 手动ack
    )
    public void sort(String msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        TimeUnit.SECONDS.sleep(3);
        log.info("{} [消费者] 接收消息: {}", DateTime.now(), msg);
        try {
            channel.basicAck(deliveryTag, false);
            // int num = 1 / 0;
        } catch (Exception e) {
            // 丢弃消息
            channel.basicReject(deliveryTag, false);
            e.printStackTrace();
        }
    }

    private final CachingConnectionFactory connectionFactory;

    @Bean(name = "mqConsumerListenerContainer")
    public SimpleRabbitListenerContainerFactory mqConsumerListenerContainer() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(this.connectionFactory);
        // 消费者每次只接收处理一条消息
        factory.setPrefetchCount(1);
        return factory;
    }

    // ----------------------------------------------------------------------------------


//    private final ConnectionFactory connectionFactory;
//
//    @SneakyThrows
//    @PostMapping("/deleteQueueValue")
//    @ApiOperation("从指定队列中删除指定值")
//    public Object deleteQueueValue(String queue, String msgValue) {
//        Channel channel = this.connectionFactory.createConnection().createChannel(false);
//        boolean isDeleted = false;
//        while (!isDeleted) {
//            GetResponse response = channel.basicGet(queue, false);
//            if (response == null) {
//                break;
//            }
//            String messageValue = new String(response.getBody(), StandardCharsets.UTF_8);
//            if (messageValue.equals(msgValue)) {
//                isDeleted = true;
//                channel.basicAck(response.getEnvelope().getDeliveryTag(), false);
//                continue;
//            }
//            channel.basicNack(response.getEnvelope().getDeliveryTag(), false, true);
//        }
//        channel.close();
//        return "SUCCESS";
//    }

}
