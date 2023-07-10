package com.zhengqing.demo.api;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@Slf4j
@Api(tags = "测试API-序列化")
@RestController
@RequestMapping("/api/mq/serialize")
@RequiredArgsConstructor
public class TestSerializeController {

    private final RocketMQTemplate rocketMQTemplate;
    public static final String TOPIC = "serialize";

    @ApiOperation("发送消息")
    @PostMapping("producer")
    public String producer() {
        this.rocketMQTemplate.convertAndSend(TOPIC,
                UserDTO.builder()
                        .userId(IdUtil.getSnowflakeNextId())
                        .username("Hello World " + DateTime.now())
                        .build()
        );
        return "SUCCESS";
    }


    @Slf4j
    @Service
    @RocketMQMessageListener(
            topic = TOPIC, // 主题，指消费者组订阅的消息服务
//        selectorExpression = "tag1", // 选择哪个标签(tag)下的信息，默认是消费该主题下的所有信息
            consumerGroup = "SerializeRocketMQListener", // 消费者组，一个组可以有多个消费者，主要的作用是集群模式负载均衡的实现，广播模式的通知的实现
            consumeMode = ConsumeMode.CONCURRENTLY, // 控制消费模式，你可以选择并发或有序接收消息
            messageModel = MessageModel.CLUSTERING // 控制消息模式，广播模式：所有消费者都能接收到信息，集群模式：无论有多少个消费者，只有一个消费者能够接收到信息，也就是说消息一旦被消费了，其它消费者就不能消费该条消息

    )
    static class SerializeRocketMQListener implements RocketMQListener<UserDTO> {

        @Override
        public void onMessage(UserDTO msg) {
            log.info("[消费者] 接收消息：{}", JSONUtil.toJsonStr(msg));
        }

    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private Long userId;
        private Integer age;
        private String username;
    }

}
