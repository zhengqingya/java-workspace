# SpringBoot整合Kafka

> https://docs.spring.io/spring-kafka/docs/current/reference/html/

#### 1、`pom.xml`中引入依赖

```
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

#### 2、`application.yml`配置

```yml
# ======================== ↓↓↓↓↓↓ kafka相关配置 ↓↓↓↓↓↓ ===============================
spring:
  kafka:
    bootstrap-servers: 127.0.0.1:9092,127.0.0.1:9093 # 127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094 # Kafka服务器的地址。集群用多个逗号分隔
    producer:
      # 消息键值的序列化
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
      compression-type: gzip  # 发送消息时的压缩类型 (none, gzip, snappy, lz4)
      retries: 3 # 生产者在发送消息时的重试次数。当发送失败或遇到可重试异常时，生产者会自动尝试重新发送消息。
      batch-size: 16384 # 生产者在进行批量发送之前等待累积的消息大小。当待发送消息的大小达到这个阈值时，生产者会将消息一起发送。默认值: 16384字节 (16KB)
      buffer-memory: 33554432 # 生产者的缓冲区大小，即用于存储待发送消息的内存大小 默认值: 32MB
      # 生产者在发送消息后等待确认的方式
      #  - 0: 生产者在发送消息后不需要等待任何来自服务器的确认。这种方式下生产者不知道消息是否成功发送，也无法处理发送失败导致的错误。该方式具备最高的性能，但也带来了潜在的数据丢失风险。
      #  - 1: 生产者在发送消息后只需要等待来自主节点（leader）的确认即可继续发送下一条消息。主节点确认意味着消息已经成功写入到主节点的本地日志中，但仍有可能由于某些原因导致数据丢失。这是默认的设置，同时兼顾了性能和数据可靠性。
      #  - -1或all: 生产者在发送消息后需要等待来自所有副本（包括主节点和副本节点）的确认。这种方式提供了最高的数据可靠性保证，但也带来了较大的延迟和吞吐量下降。
      acks: all
      properties:
        # 自定义生产者拦截器
        #        interceptor.classes: com.zhengqing.demo.config.CustomProducerInterceptor
        linger.ms: 5 # 设置消息发送的最大等待时间 单位：毫秒  -- tips: 如果设置为0ms（立即发送消息），那么batch-size参数等于无效设置
    consumer:
      # 消息键值的反序列化
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      # 如果消费者组的offset已经失效（例如，消费者加入时没有有效的offset），这个属性指定了从哪里开始消费：
      #   - earliest表示从最早的offset开始;
      #   - latest表示从最新的offset开始;
      #   - none表示如果没有找到有效的offset则抛出异常。
      auto-offset-reset: earliest
      max-poll-records: 500 # 每次拉取消息的最大记录数。用于控制每次消费的批量大小。
      group-id: my-group # 消费者所属的消费者组ID。同一个消费者组内的消费者会共享消息的消费负载。
      enable-auto-commit: true # 是否启用自动提交消费位移（offset）。如果设置为true，消费者会定期自动提交消费位移。
      auto-commit-interval: 5000ms # 消费者自动提交偏移量的时间间隔
    #      properties:
    #        # 自定义消费者拦截器
    #        interceptor.classes: com.zhengqing.demo.config.CustomConsumerInterceptor
    listener:
      # 指定消息监听器容器的类型，决定了消息的处理方式和消费者实例的创建方式
      #   - single：单一模式（默认值）。每个Kafka主题创建一个唯一的消息监听器容器，并使用单个消费者实例来处理该主题的所有分区中的消息。
      #   - batch：批量模式。每个Kafka主题创建一个唯一的消息监听器容器，并使用单个消费者实例将一批消息传递给消息处理方法进行批量处理。此模式有助于提高处理效率。
      type: single
      concurrency: 5 # 消息监听器容器的并发消费者数
```

#### 3、测试

```java
package com.zhengqing.demo.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/test/")
@RequiredArgsConstructor
@Api(tags = "测试API")
public class TestSimpleController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public static final String KAFKA_TOPIC_SIMPLE = "simple";

    @PostMapping("simple")
    @ApiOperation("简单消息")
    public String simple(@RequestParam String msg) {
        this.kafkaTemplate.send(KAFKA_TOPIC_SIMPLE, msg);
        return "SUCCESS";
    }

    @KafkaListener(topics = KAFKA_TOPIC_SIMPLE)
    public void listen(String value) {
        log.info("消费者: " + value);
    }

}
```
