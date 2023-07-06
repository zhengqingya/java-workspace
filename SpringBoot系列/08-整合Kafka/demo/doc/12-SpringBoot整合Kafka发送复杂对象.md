# SpringBoot整合Kafka发送复杂对象

#### 1、`application.yml` 修改配置

这里只贴出相关核心配置

```yml
spring:
  kafka:
    producer:
      # 消息键值的序列化
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
    consumer:
      # 消息键值的反序列化
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.trusted.packages: "*" # 配置信任的包，以便让反序列化器知道哪些类是安全的可以被反序列化的 eg: 指定包：com.zhengqing.demo；也可以 * 全部信任
```

#### 3、测试

```java
package com.zhengqing.demo.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@Slf4j
@RestController
@RequestMapping("/api/test/")
@RequiredArgsConstructor
@Api(tags = "测试API")
public class TestObjectController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public static final String KAFKA_TOPIC_OBJECT = "object";

    @PostMapping("object")
    @ApiOperation("对象消息")
    public String object(@RequestBody User user) {
        this.kafkaTemplate.send(KAFKA_TOPIC_OBJECT, user);
        return "SUCCESS";
    }

    @KafkaListener(topics = KAFKA_TOPIC_OBJECT)
    public void listen(User value) {
        log.info("消费者: " + value);
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
    }
}
```
