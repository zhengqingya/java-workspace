package com.zhengqing.demo;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * <p> App定时任务 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/8/9 11:13
 */
@Slf4j
@Component
@EnableScheduling // 开启定时任务
public class AppScheduledJobs {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Bean
    public Queue addSimpleQueue() {
        // durable: true 标识开启消息队列持久化 (队列当中的消息在重启rabbitmq服务的时候还会存在)
        return new Queue("zq_test_simple_queue", true);
    }

    /**
     * 每5秒执行一次
     *
     * @return void
     * @author zhengqingya
     * @date 2021/8/9 11:13
     */
//    @Scheduled(cron = "*/5 * * * * ?")
    public void testSimpleQueue() {
        log.info("<<<<<< test: 【{}】 >>>>>>", LocalDateTime.now());
        String msgContent = "Hello World ~ " + LocalDateTime.now();
        log.info("生产者发送消息 : " + msgContent);
        this.rabbitTemplate.convertAndSend("zq_test_simple_queue", msgContent);
    }

}

@Slf4j
@Component
class MessageHandler {

    @SneakyThrows
    @RabbitListener(queues = "zq_test_simple_queue")
    public void handleMessage(String message) {
        log.info("消费消息：  【{}】", message);
    }

}
