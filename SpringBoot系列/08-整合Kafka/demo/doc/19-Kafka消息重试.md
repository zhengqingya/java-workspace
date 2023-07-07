# Kafka消息重试

原生 Kafka 是不支持消息重试的。但是 Spring Kafka 2.7+ 封装了 Retry Topic 这个功能。

> tips: 经个人测试，默认会重试10次，但没有时间间隔，这等于没重试，在业务出现异常的时候，程序不可能立马就恢复异常...

### 一、@RetryableTopic

消费者加上注解`@RetryableTopic` -- 默认重试 3 次，间隔为 1 秒。

```
@RetryableTopic
@KafkaListener(topics = KAFKA_TOPIC)
public void listen(String value) {
    log.info("[消费者] 重试验证：{} 时间：{}", value, DateUtil.now());
    int a = 1 / 0;
    log.info("消费者: {}", value);
}
```

重试的topic为原topic加上后缀“-retry”

```shell
2023-07-07 14:42:55.906 ERROR 13936 --- [3-retry-1-0-C-1] k.r.DeadLetterPublishingRecovererFactory : Record: topic = my-retry-retry-1, partition = 0, offset = 0, main topic = my-retry threw an error at topic my-retry-retry-1 and won't be retried. Sending to DLT with name my-retry-dlt.
...
```

### 二、DLT死信队列

如果 3 次重试后依旧失败，会将消息发送到 DLT。
DLT的topic为原topic加上后缀“-dlt”

我们可以使用`@DltHandler`注解来定义进入死信队列后的操作：

```
@DltHandler
public void dltHandler(ConsumerRecord<String, String> record) {
    log.info("[DLT死信队列] topic:{}, key:{}, value:{}", record.topic(), record.key(), record.value());
}
```

### 三、自定义@RetryableTopic

可以自定义重试次数、延迟时间、topic命名策略等等，支持使用 Spring EL 表达式读取配置。

```
@RetryableTopic(
    attempts = "4",  // attempts：重试次数，默认为3。
    backoff = @Backoff(
            delay = 5000,// 消费延迟时间，单位为毫秒。
            multiplier = 2  // 延迟时间系数，此例中 attempts = 4， delay = 5000， multiplier = 2 ，则间隔时间依次为5s、10s、20s、40s，最大延迟时间受 maxDelay 限制。
    ),
    fixedDelayTopicStrategy = FixedDelayStrategy.SINGLE_TOPIC // 可选策略包括：SINGLE_TOPIC 、MULTIPLE_TOPICS
)
```

```shell
2023-07-07 14:55:44.203  INFO 17772 --- [ntainer#1-0-C-1] c.z.demo.api.TestRetryController         : [消费者] 重试验证：hello 时间：2023-07-07 14:55:44
2023-07-07 14:55:49.237  INFO 17772 --- [etry-5000-0-C-1] c.z.demo.api.TestRetryController         : [消费者] 重试验证：hello 时间：2023-07-07 14:55:49
2023-07-07 14:56:00.515  INFO 17772 --- [try-10000-0-C-1] c.z.demo.api.TestRetryController         : [消费者] 重试验证：hello 时间：2023-07-07 14:56:00
2023-07-07 14:56:21.770  INFO 17772 --- [try-20000-0-C-1] c.z.demo.api.TestRetryController         : [消费者] 重试验证：hello 时间：2023-07-07 14:56:21
2023-07-07 14:56:21.776 ERROR 17772 --- [try-20000-0-C-1] k.r.DeadLetterPublishingRecovererFactory : Record: topic = my-retry-retry-20000, partition = 0, offset = 0, main topic = my-retry threw an error at topic my-retry-retry-20000 and won't be retried. Sending to DLT with name my-retry-dlt.
```

### 四、消息重试全局配置类

如果注解和配置类同时存在，优先使用注解的配置参数，即最近优先原则。

```java
package com.zhengqing.demo.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;

@Configuration
public class KafkaRetryConfig {
    @Bean
    public RetryTopicConfiguration retryTopic(KafkaTemplate<String, String> template) {
        return RetryTopicConfigurationBuilder
                .newInstance()
                .maxAttempts(4)
                .fixedBackOff(5000)
                .includeTopics(Lists.newArrayList(
                        "my-retry"
                ))
                .create(template);
    }
}
```

> tips: 经个人测试，这个时间间隔感觉不是固定的... 可能是和主动pull消息有关吧...

```shell
2023-07-07 15:09:48.089  INFO 18576 --- [ntainer#1-0-C-1] c.z.demo.api.TestRetryController         : [消费者] 重试验证：hello 时间：2023-07-07 15:09:48
2023-07-07 15:09:53.118  INFO 18576 --- [etry-5000-0-C-1] c.z.demo.api.TestRetryController         : [消费者] 重试验证：hello 时间：2023-07-07 15:09:53
2023-07-07 15:10:05.625  INFO 18576 --- [try-10000-0-C-1] c.z.demo.api.TestRetryController         : [消费者] 重试验证：hello 时间：2023-07-07 15:10:05
2023-07-07 15:10:25.644  INFO 18576 --- [try-20000-0-C-1] c.z.demo.api.TestRetryController         : [消费者] 重试验证：hello 时间：2023-07-07 15:10:25
```
