package com.zhengqing.demo.rabbitmq.delay.dlx;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.thread.ThreadUtil;
import com.zhengqing.demo.constant.MqConstant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class DlxListener {

    @SneakyThrows(Exception.class)
    @RabbitHandler
    @RabbitListener(queues = MqConstant.ORDER_QUEUE)
    public void orderMsg(String msg) {
        log.info("----------------------------------------------");
        log.info("[消费者] 普通订单队列-接收消息: {}", msg);
        // TODO 逻辑实现...
        ThreadUtil.sleep(5, TimeUnit.SECONDS);
        throw new Exception("失败了...");
    }

    /**
     * 队列延时消费
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.DLX_QUEUE)
    public void dlxMsg(String msg) {
        log.info("----------------------------------------------");
        log.info("{} [消费者] 死信队列-接收消息: {}", DateTime.now(), msg);
    }

}

