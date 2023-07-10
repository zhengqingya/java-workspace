package com.zhengqing.demo.dynamic.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.zhengqing.demo.dynamic.service.ProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * <p> 生产者实现类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/7/10 15:36
 */
@Slf4j
public class AbsProducerService implements ProducerService {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 交换机
     */
    private String exchange;
    /**
     * 路由
     */
    private String routingKey;

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    @Override
    public void send(Object msg) {
        MessagePostProcessor messagePostProcessor = (message) -> {
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setMessageId(IdUtil.randomUUID());
            messageProperties.setTimestamp(new Date());
            return message;
        };
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentEncoding("UTF-8");
        messageProperties.setContentType("text/plain");
        String data = JSONUtil.toJsonStr(msg);
        Message message = new Message(data.getBytes(StandardCharsets.UTF_8), messageProperties);
        this.rabbitTemplate.convertAndSend(this.exchange, this.routingKey, message, messagePostProcessor);
    }

}




