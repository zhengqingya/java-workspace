package com.zhengqing.demo;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * <p> App定时任务$ </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/5/24 14:13
 */
@Slf4j
@Component
@EnableScheduling // 开启定时任务
public class AppScheduledJob {

  @Autowired
  private RocketMQTemplate rocketMQTemplate;

  /**
   * 每3秒执行一次
   *
   * @return void
   * @author zhengqingya
   * @date 2021/5/24 14:21
   */
  @Scheduled(cron = "*/3 * * * * ?")
  public void test() {
    log.info("<<<<<< test: 【{}】 >>>>>>", LocalDateTime.now());
    String msgContent = "************************ Hello World ************************";
    log.info("生产者发送消息 : " + msgContent);
    // 生产者 - 发送消息
    this.rocketMQTemplate.convertAndSend("ZQ_TOPIC:TAG1", msgContent);
  }

}

/**
 * <p> 消费者 - 接收消息 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/5/24 14:19
 */
@Slf4j
@Service
@RocketMQMessageListener(
    topic = "ZQ_TOPIC",
    consumerGroup = "ZQ-CONSUMER-GROUP",
    selectorExpression = "TAG1",
    consumeMode = ConsumeMode.ORDERLY,
    messageModel = MessageModel.CLUSTERING,
    consumeThreadMax = 1)
class MsgReceiver implements RocketMQListener<String> {

  @Override
  public void onMessage(String msg) {
    log.info("消费者接收消息 : " + msg);
  }

}
