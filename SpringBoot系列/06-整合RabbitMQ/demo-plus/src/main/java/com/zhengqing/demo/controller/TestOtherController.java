package com.zhengqing.demo.controller;

import cn.hutool.core.date.DateTime;
import com.zhengqing.demo.util.MqUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Api(tags = "其它测试")
@RestController
@RequestMapping("/api/test")
public class TestOtherController {

    @Resource
    private AmqpTemplate rabbitTemplate;

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

//    @Resource
//    private ConnectionFactory connectionFactory;
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
