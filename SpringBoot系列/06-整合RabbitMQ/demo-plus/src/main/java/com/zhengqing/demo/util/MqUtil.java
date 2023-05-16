package com.zhengqing.demo.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * MQ 工具类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2019/11/27 14:38
 */
@Slf4j
@Component
public class MqUtil {

    private static RabbitTemplate rabbitTemplate;

    @Autowired
    public MqUtil(RabbitTemplate rabbitTemplate) {
        MqUtil.rabbitTemplate = rabbitTemplate;
    }


    /**
     * 发送普通消息 （默认交换机）
     *
     * @param exchange   交换机
     * @param routingKey 路由key
     * @param msgData    消息
     * @return void
     * @author zhengqingya
     * @date 2022/7/8 12:47
     */
    public static void send(String exchange, String routingKey, Object msgData) {
        log.info("[MQ生产者] 交换机:[{}] 路由key:[{}] 发送消息:[{}]", exchange, routingKey, JSONUtil.toJsonStr(msgData));
        rabbitTemplate.convertAndSend(exchange, routingKey, msgData);
    }

    /**
     * 延迟队列
     *
     * @param exchange         交换机
     * @param routingKey       路由key
     * @param msgData          消息
     * @param delayMillisecond 消息延时时间 单位：毫秒
     * @return void
     * @author zhengqingya
     * @date 2022/7/8 12:40
     */
    public static void sendDelay(String exchange, String routingKey, Object msgData, long delayMillisecond) {
        log.info("[MQ生产者] 交换机:[{}] 路由key:[{}] 发送消息:[{}] 延时[{}]毫秒", exchange, routingKey, JSONUtil.toJsonStr(msgData), delayMillisecond);
        Assert.isTrue(delayMillisecond <= 4294967295L, "MQ最大延时4294967295毫秒(即49.7103天)");
        rabbitTemplate.convertAndSend(exchange, routingKey, msgData, message -> {
            message.getMessageProperties().setHeader("x-delay", delayMillisecond);
            return message;
        });
    }

}
