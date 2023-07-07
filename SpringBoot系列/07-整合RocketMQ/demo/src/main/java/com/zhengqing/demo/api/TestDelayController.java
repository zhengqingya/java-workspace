package com.zhengqing.demo.api;


import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description 定时消息是 Apache RocketMQ 提供的一种高级消息类型，消息被发送至服务端后，在指定时间后才能被消费者消费。通过设置一定的定时时间可以实现分布式场景的延时调度触发效果。
 * @date 2021/10/5 2:36 下午
 */
@Slf4j
@RestController
@RequestMapping("api/test/")
@RequiredArgsConstructor
@Api(tags = "测试API-定时/延时消息（暂无效）")
public class TestDelayController {

    private final RocketMQTemplate rocketMQTemplate;

    public static final String TOPIC = "delay";
    // 主题+tag，中间用“:”分隔，主要是用于消息的过滤，比如说在消费的时候，只消费tag1标签下的消息
    private static final String TOPIC_TAG = TOPIC.concat(":tag1");

    @ApiOperation("发送延迟消息")
    @PostMapping("/syncSendDelayTime")
    public String syncSendDelayTime() {
        String msgContent = DateUtil.now();
        log.info("[生产者] 发送消息：{}", msgContent);
        // 3秒后才能消费
        this.rocketMQTemplate.syncSendDelayTimeSeconds(TOPIC_TAG, "3秒：" + msgContent, 3L);
        // 3毫秒后才能消费
        this.rocketMQTemplate.syncSendDelayTimeMills(TOPIC_TAG, "3毫秒：" + msgContent, 3L);
        return "OK";
    }

    @ApiOperation("定时处理")
    @PostMapping("/syncSendDeliverTimeMills")
    public String syncSendDeliverTimeMills() {
        String msgContent = DateUtil.now();
        log.info("[生产者] 发送消息：{}", msgContent);
        // 这里为了方便看到效果，模拟定时3秒后触发
        long deliverTimeMills = System.currentTimeMillis() + 3000;
        this.rocketMQTemplate.syncSendDeliverTimeMills(TOPIC_TAG, msgContent, deliverTimeMills);
        return "OK";
    }


}
