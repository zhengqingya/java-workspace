package com.zhengqing.demo.api;

import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p> kafka </p>
 *
 * @author zhengqing
 * @description
 * @date 2020/4/21 22:34
 */
@Slf4j
@RestController
@RequestMapping("/api/test/")
@RequiredArgsConstructor
@Api(tags = "测试API")
public class TestBatchMsgController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public static final String KAFKA_TOPIC = "batch_msg_topic";

    @PostMapping("batch-msg")
    @ApiOperation("批量消息")
    public String object(@RequestBody User user) {
        log.info("生产者: " + JSONUtil.toJsonStr(user));
        String name = user.getName();
        for (int i = 0; i < user.getNum(); i++) {
            user.setName(name + i);
            this.kafkaTemplate.send(KAFKA_TOPIC, user);
        }
        return "SUCCESS";
    }

    @KafkaListener(topics = KAFKA_TOPIC, containerFactory = "batchMsgFactory", groupId = "batchGroup1")
    public void listen1(List<User> msgList) {
        log.info("消费者-批量消费1: " + JSONUtil.toJsonStr(msgList));
    }


    @KafkaListener(topics = KAFKA_TOPIC, containerFactory = "batchMsgFactory", groupId = "batchGroup2")
    public void listen2(List<ConsumerRecord<String, User>> records) {
        List<User> list = records.stream().map(ConsumerRecord::value).collect(Collectors.toList());
        log.info("消费者-批量消费2：消息数量：{}, 消息内容：{}", records.size(), JSONUtil.toJsonStr(list));
    }

//    @KafkaListener(topics = KAFKA_TOPIC, containerFactory = "batchMsgFactory", groupId = "batchGroup3")
//    public void listen3(List<ConsumerRecord<String, String>> records) {
//        List<User> list = records.stream().map(e -> JSONUtil.toBean(e.value(), User.class)).collect(Collectors.toList());
//        log.info("消费者-批量消费3：消息数量：{}, 消息内容：{}", records.size(), JSONUtil.toJsonStr(list));
//    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> batchMsgFactory(ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        // 设置为批量监听
        factory.setBatchListener(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
        return factory;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    static class User implements Serializable {
        private static final long serialVersionUID = 1L;
        @ApiModelProperty(example = "666")
        private String id;
        @ApiModelProperty(example = "zhengqingya")
        private String name;
        @ApiModelProperty(example = "10")
        private Integer num;
    }
}
