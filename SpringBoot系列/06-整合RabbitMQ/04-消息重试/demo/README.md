# 消息重试机制

消费者消费消息的时候，发生异常情况，导致消息未确认， 该消息会被重复消费(默认没有重复次数，即无限循环消费)
，但可以通过设置重试次数以及达到重试次数之后的消息处理

实现步骤：

1. 开启重试
2. 修改消息失败策略为重新发布到新队列 或 转入死信队列 （tips:如果修改了失败策略则死信队列无法生效，即两者并不兼容...）

### 一、application.yml 配置

> tips: `spring.rabbitmq.listener.simple.retry.enabled`默认false 即一直死循环消费

```yml
# RabbitMQ配置
spring:
  rabbitmq:
    host: 127.0.0.1
    port: 5672
    # 填写自己安装rabbitmq时设置的账号密码，默认账号密码为`guest`
    username: admin
    password: admin
    virtual-host: my_vhost # 填写自己的虚拟机名，对应可查看 `127.0.0.1:15672/#/users` 下Admin中的`Can access virtual hosts`信息
    # 消息接收方
    listener:
      simple:
        acknowledge-mode: auto # 表示消息确认方式，其有三种配置方式，分别是none、manual（手动ack）和auto；默认auto
        concurrency: 5         # 最小的消费者数量
        max-concurrency: 10    # 最大的消费者数量
        prefetch: 3            # 指定一个请求能处理多少个消息，如果有事务的话，必须大于等于transaction数量.
        # 重试机制：
        #         eg: 最大重试次数为8 & 重试间隔1秒 & 间隔时间乘子2 & 最大间隔时间50秒  -- (最大重试次数包含初次消费)
        #         初次消費
        #         第1次：1秒
        #         第2次：1*2=2秒
        #         第3次：2*2=4秒
        #         第4次：4*2=8秒
        #         第5次：8*2=16秒
        #         第6次：16*2=32秒
        #         第7次：32*2=64秒 (由于设置最大间隔时间，因此这里为50秒 )
        retry:
          enabled: true          # 是否开启重试
          max-attempts: 4        # 最大重试次数
          max-interval: 50000    # 重试最大间隔时间
          initial-interval: 1000 # 重试间隔（单位：毫秒）
          multiplier: 2          # 间隔时间乘子，间隔时间*乘子=下一次的间隔时间，最大不能超过设置的最大间隔时间
```

### 二、修改消息失败策略

```java
package com.zhengqing.demo.config;

import org.springframework.amqp.rabbit.config.AbstractRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.boot.autoconfigure.amqp.AbstractRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String RETRY_EXCHANGE = "retry_exchange";
    public static final String RETRY_FAILURE_KEY = "retry_fail_routing_key";
    public static final String RETRY_FAILURE_QUEUE = "retry_fail_queue";

    /**
     * 修改消息失败策略
     * 默认配置： {@link AbstractRabbitListenerContainerFactoryConfigurer#configure(AbstractRabbitListenerContainerFactory, ConnectionFactory, RabbitProperties.AmqpContainer)}
     * MessageRecoverer recoverer = this.messageRecoverer != null ? this.messageRecoverer : new RejectAndDontRequeueRecoverer(); 默认拒绝&不重新排队
     */
    @Bean
    public MessageRecoverer messageRecoverer(RabbitTemplate rabbitTemplate) {
        /**
         return new RejectAndDontRequeueRecoverer(); // 拒绝&不重新排队(默认)
         return new MessageBatchRecoverer() {public void recover(List<Message> messages, Throwable cause) {}}; // 用于消息批量处理的恢复器（Recoverer），它可以在消息消费失败时对一个批量的消息进行统一的处理。
         return new ImmediateRequeueMessageRecoverer(); // 重新排队 -- 重试之后，返回队列，然后再重试，周而复始直到不抛出异常为止，这样还是会影响后续的消息消费...
         return new RepublishMessageRecoverer(rabbitTemplate, RETRY_EXCHANGE, RETRY_FAILURE_KEY); // 重新发布 -- 重试之后，将消息转发到重试失败队列，由重试失败消费者消费...
         */
        return new RepublishMessageRecoverer(rabbitTemplate, RETRY_EXCHANGE, RETRY_FAILURE_KEY);
    }

}
```

消息重试失败重发队列 -- 业务补偿机制

```java
package com.zhengqing.demo.config;

import cn.hutool.core.date.DateTime;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class RetryFailConsumer {

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitMqConfig.RETRY_FAILURE_QUEUE, durable = "true"),
                    exchange = @Exchange(value = RabbitMqConfig.RETRY_EXCHANGE, type = "direct", durable = "true"),
                    key = RabbitMqConfig.RETRY_FAILURE_KEY
            )
    )
    public void retryFailConsumer(Message message, Channel channel) throws Exception {
        log.info("[消息重试失败] 接收时间: {} 接收消息: {}", DateTime.now(), new String(message.getBody(), StandardCharsets.UTF_8));
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            log.error("[消息重试失败] 异常:{}", e.getMessage());
            // 如果这里再抛出异常则继续走消息重试...
//            throw e;
        }
    }

}
```

### 三、测试

```java
package com.zhengqing.demo.controller;

import cn.hutool.core.date.DateTime;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "测试mq-消息重试")
@RestController
@RequestMapping("/api/mq/retry")
@RequiredArgsConstructor
public class RetryController {
    private final RabbitTemplate rabbitTemplate;

    @ApiOperation("消息重试")
    @PostMapping("producer")
    public String producer() {
        String msgContent = "Hello World " + DateTime.now();
        log.info("{} [生产者] 发送消息: {}", DateTime.now(), msgContent);
        this.rabbitTemplate.convertAndSend("test_exchange", "test_routing_key_retry", msgContent);
        return "SUCCESS";
    }

    @SneakyThrows
    @RabbitHandler
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "test_queue_retry", durable = "true"),
                    exchange = @Exchange(value = "test_exchange", type = "direct", durable = "true"),
                    key = "test_routing_key_retry"
            )
    )
    public void consumer(String msg) {
        log.info("{} [消费者] 接收消息: {}", DateTime.now(), msg);
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            log.error("[消费者] 异常:{}", e.getMessage());
            throw e;
        }
    }

}
```

### 四、MessageRecoverer 消息失败策略

MessageRecoverer 是一个接口，用于在消息消费失败时执行自定义的恢复逻辑。
当消费者在处理消息时抛出异常时，消息队列系统会调用 MessageRecoverer 来处理该消息。

具体来说，MessageRecoverer 的作用包括：

1. 消息重试：一种常见的方式是在消息消费失败后，将消息重新放回队列等待重试。
   通过实现 MessageRecoverer 接口，可以在恢复逻辑中选择将消息重新发送到队列，使其可以被后续的消费者再次处理。
2. 错误日志记录：另一种常见的处理方式是将消息消费失败的相关信息记录下来，例如保存到日志文件或数据库中，以供后续的错误分析和排查。
   通过实现 MessageRecoverer 接口，可以在恢复逻辑中将失败的消息信息进行记录，方便后续的故障排查。
3. 异常补偿：有些场景下，消息消费失败后可能需要执行特定的补偿操作，例如向其他系统发送通知、回滚事务、释放资源等。
   通过实现 MessageRecoverer 接口，可以在恢复逻辑中执行这些额外的补偿操作，确保系统状态的一致性和可靠性。

需要注意的是，具体如何使用 MessageRecoverer 取决于所使用的消息队列系统和相关框架的支持。
例如，在 RabbitMQ 中，可以使用 Spring AMQP 提供的 RabbitTemplate 来配置 MessageRecoverer，并指定恢复策略、重试次数等参数。

总之，MessageRecoverer 提供了处理消息消费失败的灵活性，可以根据实际需求定义自定义的恢复逻辑，以保证消息传递的可靠性和系统的稳定性。

#### 1、new RejectAndDontRequeueRecoverer(); 默认拒绝&不重新排队

重试4次之后，完事...

```shell
2023-07-10 10:59:57.010  INFO 52744 --- [ntContainer#0-7] c.z.demo.controller.RetryController      : 2023-07-10 10:59:57 [消费者] 接收消息: Hello World 2023-07-10 10:59:14
2023-07-10 10:59:58.012  INFO 52744 --- [ntContainer#0-7] c.z.demo.controller.RetryController      : 2023-07-10 10:59:58 [消费者] 接收消息: Hello World 2023-07-10 10:59:14
2023-07-10 11:00:00.013  INFO 52744 --- [ntContainer#0-7] c.z.demo.controller.RetryController      : 2023-07-10 11:00:00 [消费者] 接收消息: Hello World 2023-07-10 10:59:14
2023-07-10 11:00:04.013  INFO 52744 --- [ntContainer#0-7] c.z.demo.controller.RetryController      : 2023-07-10 11:00:04 [消费者] 接收消息: Hello World 2023-07-10 10:59:14
2023-07-10 11:00:04.013  WARN 52744 --- [ntContainer#0-7] s.a.r.r.ImmediateRequeueMessageRecoverer : Retries exhausted for message (Body:'Hello World 2023-07-10 10:59:14' MessageProperties [headers={}, contentType=text/plain, contentEncoding=UTF-8, contentLength=0, receivedDeliveryMode=PERSISTENT, priority=0, redelivered=true, receivedExchange=test_exchange, receivedRoutingKey=test_routing_key_retry, deliveryTag=1, consumerTag=amq.ctag-NGyDo3jLP5I0r_0AK01mqg, consumerQueue=test_queue_retry]); requeuing...

org.springframework.amqp.rabbit.support.ListenerExecutionFailedException: Listener method 'public void com.zhengqing.demo.controller.RetryController.consumer(java.lang.String)' threw exception
	at org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter.invokeHandler(MessagingMessageListenerAdapter.java:228)
...
```

#### 2、new ImmediateRequeueMessageRecoverer(); 重新排队

重试4次之后，返回队列，然后再重试4次，周而复始直到不抛出异常为止，这样还是会影响后续的消息消费...

```shell
2023-07-10 10:59:57.010  INFO 52744 --- [ntContainer#0-7] c.z.demo.controller.RetryController      : 2023-07-10 10:59:57 [消费者] 接收消息: Hello World 2023-07-10 10:59:14
2023-07-10 10:59:58.012  INFO 52744 --- [ntContainer#0-7] c.z.demo.controller.RetryController      : 2023-07-10 10:59:58 [消费者] 接收消息: Hello World 2023-07-10 10:59:14
2023-07-10 11:00:00.013  INFO 52744 --- [ntContainer#0-7] c.z.demo.controller.RetryController      : 2023-07-10 11:00:00 [消费者] 接收消息: Hello World 2023-07-10 10:59:14
2023-07-10 11:00:04.013  INFO 52744 --- [ntContainer#0-7] c.z.demo.controller.RetryController      : 2023-07-10 11:00:04 [消费者] 接收消息: Hello World 2023-07-10 10:59:14
2023-07-10 11:00:04.013  WARN 52744 --- [ntContainer#0-7] s.a.r.r.ImmediateRequeueMessageRecoverer : Retries exhausted for message (Body:'Hello World 2023-07-10 10:59:14' MessageProperties [headers={}, contentType=text/plain, contentEncoding=UTF-8, contentLength=0, receivedDeliveryMode=PERSISTENT, priority=0, redelivered=true, receivedExchange=test_exchange, receivedRoutingKey=test_routing_key_retry, deliveryTag=1, consumerTag=amq.ctag-NGyDo3jLP5I0r_0AK01mqg, consumerQueue=test_queue_retry]); requeuing...

org.springframework.amqp.rabbit.support.ListenerExecutionFailedException: Listener method 'public void com.zhengqing.demo.controller.RetryController.consumer(java.lang.String)' threw exception
	at org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter.invokeHandler(MessagingMessageListenerAdapter.java:228)
...

2023-07-10 11:00:04.016  INFO 52744 --- [ntContainer#0-8] c.z.demo.controller.RetryController      : 2023-07-10 11:00:04 [消费者] 接收消息: Hello World 2023-07-10 10:59:14
2023-07-10 11:00:05.017  INFO 52744 --- [ntContainer#0-8] c.z.demo.controller.RetryController      : 2023-07-10 11:00:05 [消费者] 接收消息: Hello World 2023-07-10 10:59:14
2023-07-10 11:00:07.017  INFO 52744 --- [ntContainer#0-8] c.z.demo.controller.RetryController      : 2023-07-10 11:00:07 [消费者] 接收消息: Hello World 2023-07-10 10:59:14
2023-07-10 11:00:11.018  INFO 52744 --- [ntContainer#0-8] c.z.demo.controller.RetryController      : 2023-07-10 11:00:11 [消费者] 接收消息: Hello World 2023-07-10 10:59:14
...
```

#### 3、new RepublishMessageRecoverer(rabbitTemplate, ERROR_EXCHANGE, ERROR_ROUTING_KEY); 重新发布 ☆☆☆

重试4次之后，将消息转发到重试失败队列，由重试失败消费者消费...

```shell
2023-07-10 11:20:38.850  INFO 55276 --- [ntContainer#1-1] c.z.demo.controller.RetryController      : 2023-07-10 11:20:38 [消费者] 接收消息: Hello World 2023-07-10 11:20:38
2023-07-10 11:20:39.852  INFO 55276 --- [ntContainer#1-1] c.z.demo.controller.RetryController      : 2023-07-10 11:20:39 [消费者] 接收消息: Hello World 2023-07-10 11:20:38
2023-07-10 11:20:41.852  INFO 55276 --- [ntContainer#1-1] c.z.demo.controller.RetryController      : 2023-07-10 11:20:41 [消费者] 接收消息: Hello World 2023-07-10 11:20:38
2023-07-10 11:20:45.853  INFO 55276 --- [ntContainer#1-1] c.z.demo.controller.RetryController      : 2023-07-10 11:20:45 [消费者] 接收消息: Hello World 2023-07-10 11:20:38
2023-07-10 11:20:45.856  WARN 55276 --- [ntContainer#1-1] o.s.a.r.retry.RepublishMessageRecoverer  : Republishing failed message to exchange 'retry_exchange' with routing key retry_fail_routing_key
2023-07-10 11:20:45.858  INFO 55276 --- [ntContainer#0-2] c.z.demo.config.RetryFailConsumer        : [消息重试失败] 接收时间: 2023-07-10 11:20:45 接收消息: Hello World 2023-07-10 11:20:38
```

---

### 五、转入死信队列

tips: 注意和消息失败策略并不兼容

```java
package com.zhengqing.demo.controller;

import cn.hutool.core.date.DateTime;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "测试mq-消息重试")
@RestController
@RequestMapping("/api/mq/retry/dlx")
@RequiredArgsConstructor
public class RetryFailToDlxController {
    private final RabbitTemplate rabbitTemplate;

    @ApiOperation("消息重试失败转死信队列")
    @PostMapping("producer")
    public String producer() {
        String msgContent = "Hello World " + DateTime.now();
        log.info("{} [生产者] 发送消息: {}", DateTime.now(), msgContent);
        this.rabbitTemplate.convertAndSend("test_exchange", "test_routing_key_retry_to_dlx", msgContent);
        return "SUCCESS";
    }

    /**
     * 普通队列消费
     */
    @SneakyThrows
    @RabbitHandler
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "test_queue_retry_to_dlx", durable = "true"
                            , arguments = {
                            @Argument(name = "x-dead-letter-exchange", value = "dlx_exchange"),
                            @Argument(name = "x-message-ttl", value = "1800000", type = "java.lang.Long"),
                            @Argument(name = "x-dead-letter-routing-key", value = "test_routing_key_dlx")
                    }
                    ),
                    exchange = @Exchange(value = "test_exchange", type = "direct", durable = "true"),
                    key = "test_routing_key_retry_to_dlx"
            )
    )
    public void consumer(String msg) {
        log.info("{} [消费者] 接收消息: {}", DateTime.now(), msg);
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            log.error("[消费者] 异常:{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 普通队列消息重试依然失败转入死信队列进行消息补偿机制
     */
    @RabbitListener(
            bindings = {
                    @QueueBinding(
                            value = @Queue(name = "test_queue_dlx", durable = "true"),
                            exchange = @Exchange(value = "dlx_exchange", type = "direct", durable = "true"),
                            key = "test_routing_key_dlx"
                    )
            }
    )
    public void dlx(String msg) throws Exception {
        log.info("[死信队列] 接收消息: {}", msg);
        // 如果死信队列异常 重试一轮之后完事... 不会无限轮重试
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            log.error("[死信队列] 异常:{}", e.getMessage());
            throw e;
        }
    }

}
```

测试日志

```shell
2023-07-11 10:21:19.035  INFO 52308 --- [p-nio-80-exec-1] c.z.d.c.RetryFailToDlxController         : 2023-07-11 10:21:19 [生产者] 发送消息: Hello World 2023-07-11 10:21:19
2023-07-11 10:21:19.047  INFO 52308 --- [ntContainer#2-1] c.z.d.c.RetryFailToDlxController         : 2023-07-11 10:21:19 [消费者] 接收消息: Hello World 2023-07-11 10:21:19
2023-07-11 10:21:19.048 ERROR 52308 --- [ntContainer#2-1] c.z.d.c.RetryFailToDlxController         : [消费者] 异常:/ by zero
2023-07-11 10:21:20.049  INFO 52308 --- [ntContainer#2-1] c.z.d.c.RetryFailToDlxController         : 2023-07-11 10:21:20 [消费者] 接收消息: Hello World 2023-07-11 10:21:19
2023-07-11 10:21:20.049 ERROR 52308 --- [ntContainer#2-1] c.z.d.c.RetryFailToDlxController         : [消费者] 异常:/ by zero
2023-07-11 10:21:22.051  INFO 52308 --- [ntContainer#2-1] c.z.d.c.RetryFailToDlxController         : 2023-07-11 10:21:22 [消费者] 接收消息: Hello World 2023-07-11 10:21:19
2023-07-11 10:21:22.051 ERROR 52308 --- [ntContainer#2-1] c.z.d.c.RetryFailToDlxController         : [消费者] 异常:/ by zero
2023-07-11 10:21:26.051  INFO 52308 --- [ntContainer#2-1] c.z.d.c.RetryFailToDlxController         : 2023-07-11 10:21:26 [消费者] 接收消息: Hello World 2023-07-11 10:21:19
2023-07-11 10:21:26.051 ERROR 52308 --- [ntContainer#2-1] c.z.d.c.RetryFailToDlxController         : [消费者] 异常:/ by zero
2023-07-11 10:21:26.053  WARN 52308 --- [ntContainer#2-1] o.s.a.r.r.RejectAndDontRequeueRecoverer  : Retries exhausted for message (Body:'Hello World 2023-07-11 10:21:19' MessageProperties [headers={}, contentType=text/plain, contentEncoding=UTF-8, contentLength=0, receivedDeliveryMode=PERSISTENT, priority=0, redelivered=false, receivedExchange=test_exchange, receivedRoutingKey=test_routing_key_retry_to_dlx, deliveryTag=1, consumerTag=amq.ctag-L6XkKHMmMDfo_xczK1hSmg, consumerQueue=test_queue_retry_to_dlx])

org.springframework.amqp.rabbit.support.ListenerExecutionFailedException: Listener method 'public void com.zhengqing.demo.controller.RetryFailToDlxController.consumer(java.lang.String)' threw exception
	at org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter.invokeHandler(MessagingMessageListenerAdapter.java:228)
...

2023-07-11 10:21:26.057  INFO 52308 --- [ntContainer#3-1] c.z.d.c.RetryFailToDlxController         : [死信队列] 接收消息: Hello World 2023-07-11 10:21:19
2023-07-11 10:21:26.057 ERROR 52308 --- [ntContainer#3-1] c.z.d.c.RetryFailToDlxController         : [死信队列] 异常:/ by zero
2023-07-11 10:21:27.058  INFO 52308 --- [ntContainer#3-1] c.z.d.c.RetryFailToDlxController         : [死信队列] 接收消息: Hello World 2023-07-11 10:21:19
2023-07-11 10:21:27.058 ERROR 52308 --- [ntContainer#3-1] c.z.d.c.RetryFailToDlxController         : [死信队列] 异常:/ by zero
2023-07-11 10:21:29.058  INFO 52308 --- [ntContainer#3-1] c.z.d.c.RetryFailToDlxController         : [死信队列] 接收消息: Hello World 2023-07-11 10:21:19
2023-07-11 10:21:29.058 ERROR 52308 --- [ntContainer#3-1] c.z.d.c.RetryFailToDlxController         : [死信队列] 异常:/ by zero
2023-07-11 10:21:33.059  INFO 52308 --- [ntContainer#3-1] c.z.d.c.RetryFailToDlxController         : [死信队列] 接收消息: Hello World 2023-07-11 10:21:19
2023-07-11 10:21:33.059 ERROR 52308 --- [ntContainer#3-1] c.z.d.c.RetryFailToDlxController         : [死信队列] 异常:/ by zero
2023-07-11 10:21:33.059  WARN 52308 --- [ntContainer#3-1] o.s.a.r.r.RejectAndDontRequeueRecoverer  : Retries exhausted for message (Body:'Hello World 2023-07-11 10:21:19' MessageProperties [headers={x-first-death-exchange=test_exchange, x-death=[{reason=rejected, count=1, exchange=test_exchange, time=Tue Jul 11 10:21:26 CST 2023, routing-keys=[test_routing_key_retry_to_dlx], queue=test_queue_retry_to_dlx}], x-first-death-reason=rejected, x-first-death-queue=test_queue_retry_to_dlx}, contentType=text/plain, contentEncoding=UTF-8, contentLength=0, receivedDeliveryMode=PERSISTENT, priority=0, redelivered=false, receivedExchange=dlx_exchange, receivedRoutingKey=test_routing_key_dlx, deliveryTag=1, consumerTag=amq.ctag-OWW27sAur6jpI_X9W516jQ, consumerQueue=test_queue_dlx])

org.springframework.amqp.rabbit.support.ListenerExecutionFailedException: Listener method 'public void com.zhengqing.demo.controller.RetryFailToDlxController.dlx(java.lang.String) throws java.lang.Exception' threw exception
	at org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter.invokeHandler(MessagingMessageListenerAdapter.java:228)
...
```