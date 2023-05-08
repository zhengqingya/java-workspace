package com.zhengqing.demo.canal.mq;

import com.alibaba.fastjson.JSON;
import com.zhengqing.demo.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * <p> 监听数据库数据变化 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/1/20 15:07
 */
@Slf4j
@Component
public class CanalRabbitMqListener {

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(value = MqConstant.CANAL_QUEUE, durable = "true"),
                    exchange = @Exchange(value = MqConstant.CANAL_EXCHANGE),
                    key = MqConstant.CANAL_ROUTING_KEY
            )
    })
    public void handleCanalDataChange(String message) {
        log.info("[canal] 接收消息: {}", JSON.toJSONString(message));
    }

}
