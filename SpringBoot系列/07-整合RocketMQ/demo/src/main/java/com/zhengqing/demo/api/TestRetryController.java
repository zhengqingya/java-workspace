package com.zhengqing.demo.api;


import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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

/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/10/5 2:36 下午
 */
@Slf4j
@RestController
@RequestMapping("api/test/")
@RequiredArgsConstructor
@Api(tags = "测试API-消息重试")
public class TestRetryController {

    private final RocketMQTemplate rocketMQTemplate;

    public static final String TOPIC = "retry";

    @ApiOperation("消息重试")
    @PostMapping("/retry")
    public String retry() {
        String msgContent = DateUtil.now();
        log.info("[生产者] 发送消息：{}", msgContent);
        this.rocketMQTemplate.convertAndSend(TOPIC, msgContent);
        return "OK";
    }

    @Slf4j
    @Service
    @RocketMQMessageListener(
            topic = TOPIC, // 主题，指消费者组订阅的消息服务
//        selectorExpression = "tag1", // 选择哪个标签(tag)下的信息，默认是消费该主题下的所有信息
            consumerGroup = "retry-consumer-group", // 消费者组，一个组可以有多个消费者，主要的作用是集群模式负载均衡的实现，广播模式的通知的实现
            consumeMode = ConsumeMode.CONCURRENTLY, // 控制消费模式，你可以选择并发或有序接收消息
            messageModel = MessageModel.CLUSTERING // 控制消息模式，广播模式：所有消费者都能接收到信息，集群模式：无论有多少个消费者，只有一个消费者能够接收到信息，也就是说消息一旦被消费了，其它消费者就不能消费该条消息

    )
    static class RetryRocketMQListener implements RocketMQListener<String> {

        @Override
        public void onMessage(String msg) {
            log.info("[消费者] 接收消息：{}", msg);
            int a = 1 / 0;
        }

    }

}

