package com.zhengqing.demo.api;


import cn.hutool.core.date.DateUtil;
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
 * @description 普通消息为 Apache RocketMQ 中最基础的消息，普通消息一般应用于微服务解耦、事件驱动、数据集成等场景，这些场景大多数要求数据传输通道具有可靠传输的能力，且对消息的处理时机、处理顺序没有特别要求。
 * @date 2021/10/5 2:36 下午
 */
@Slf4j
@RestController
@RequestMapping("api/test/")
@RequiredArgsConstructor
@Api(tags = "测试API-普通消息")
public class TestSimpleController {

    private final RocketMQTemplate rocketMQTemplate;

    public static final String TOPIC = "test";
    // 主题+tag，中间用“:”分隔，主要是用于消息的过滤，比如说在消费的时候，只消费tag1标签下的消息
//    private static final String TOPIC = "test:tag1";

    @ApiOperation("普通消息")
    @PostMapping("/hello")
    public String hello() {
        String msgContent = DateUtil.now();
        log.info("[生产者] 发送消息：{}", msgContent);
        this.rocketMQTemplate.convertAndSend(TOPIC, msgContent);
        return "OK";
    }

    @ApiOperation("单向消息")
    @PostMapping("/sendOneWay")
    public String sendOneWay() {
        // 发送消息后，不需要等待Broker的响应，直接返回。这种方式适用于不需要关注消息发送结果的场景，如日志记录、统计信息等。
        for (int i = 0; i < 3; i++) {
            String msgContent = DateUtil.now();
            log.info("[生产者] 发送消息：{}", msgContent);
            this.rocketMQTemplate.sendOneWay(TOPIC, msgContent);
        }
        return "OK";
    }

    @ApiOperation("同步消息")
    @PostMapping("/syncSend")
    public Object syncSend() {
        // syncSend方法会阻塞当前线程，直到消息发送完成并收到了消息服务器的响应。如果消息发送成功，syncSend方法会返回一个SendResult对象，包含了消息的发送状态、消息ID等信息。如果消息发送失败，syncSend方法会抛出一个MessagingException异常。
        String msgContent = DateUtil.now();
        log.info("[生产者] 发送消息：{}", msgContent);
        SendResult sendResult = this.rocketMQTemplate.syncSend(TOPIC, msgContent);
        return sendResult;
    }

    @ApiOperation("异步消息")
    @PostMapping("/asyncSend")
    public String asyncSend() {
        // asyncSend方法不会阻塞当前线程，而是在另一个线程中异步发送消息。因此，asyncSend方法会立即返回，不会等待消息发送完成。如果需要等待消息发送完成并处理发送结果，可以使用SendCallback回调接口。
        String msgContent = DateUtil.now();
        log.info("[生产者] 发送消息：{}", msgContent);
        this.rocketMQTemplate.asyncSend(TOPIC, msgContent, new SendCallback() {
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
