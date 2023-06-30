package com.zhengqing.demo.kafka;

import com.zhengqing.demo.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * <p> 消费者 - 接收消息 </p>
 *
 * @author zhengqing
 * @description 监听kafka消息
 * @date 2020/4/21 22:34
 */
@Slf4j
@Component
public class Consumer {

//    @KafkaListener(topics = Constants.KAFKA_TOPIC_HELLO)
//    public void listen(ConsumerRecord<?, ?> record) {
//        log.info("topic: " + record.topic() + "  <|============|>  消息内容：" + record.value());
//    }

    @KafkaListener(topics = Constants.KAFKA_TOPIC_HELLO)
    public void listen(String value) {
        log.info("消费者: " + value);
    }

}
