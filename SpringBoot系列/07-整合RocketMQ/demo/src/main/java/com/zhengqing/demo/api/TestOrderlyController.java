package com.zhengqing.demo.api;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description 顺序消息是 Apache RocketMQ 提供的一种高级消息类型，支持消费者按照发送消息的先后顺序获取消息，从而实现业务场景中的顺序处理。 相比其他类型消息，顺序消息在发送、存储和投递的处理过程中，更多强调多条消息间的先后顺序关系。
 * @date 2021/10/5 2:36 下午
 */
@Slf4j
@RestController
@RequestMapping("api/test/")
@RequiredArgsConstructor
@Api(tags = "测试API-顺序消息")
public class TestOrderlyController {

    private final RocketMQTemplate rocketMQTemplate;

    public static final String TOPIC = "orderly";
    // 主题+tag，中间用“:”分隔，主要是用于消息的过滤，比如说在消费的时候，只消费tag1标签下的消息
    private static final String TOPIC_TAG = TOPIC.concat(":tag1");

    @ApiOperation("单向顺序消息")
    @PostMapping("/sendOneWayOrderly")
    public String sendOneWayOrderly() {
        String msgContent = DateUtil.now();
        log.info("[生产者] 发送消息：{}", msgContent);
        this.rocketMQTemplate.sendOneWayOrderly(TOPIC_TAG, msgContent, IdUtil.getSnowflakeNextIdStr());
        return "OK";
    }

    @ApiOperation("同步发送顺序消息")
    @PostMapping("/syncSendOrderly")
    public String syncSendOrderly() {
        String msgContent = DateUtil.now();
        log.info("[生产者] 发送消息：{}", msgContent);
        this.rocketMQTemplate.syncSendOrderly(TOPIC_TAG, msgContent, IdUtil.getSnowflakeNextIdStr());
        return "OK";
    }

    @ApiOperation("异步发送顺序消息")
    @PostMapping("/asyncSendOrderly")
    public String asyncSendOrderly() {
        String msgContent = DateUtil.now();
        log.info("[生产者] 发送消息：{}", msgContent);
        this.rocketMQTemplate.asyncSendOrderly(TOPIC_TAG, msgContent, IdUtil.getSnowflakeNextIdStr(), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("[生产者] 回调结果：{}", sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("[生产者] 消息发送异常：{}", throwable.getMessage());
            }
        });
        return "OK";
    }


}
