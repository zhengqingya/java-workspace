# 手动ACK

### 一、开启手动ACK

org.springframework.amqp.core.AcknowledgeMode（确认模式）: 用于定义如何确认消息的传递

- `NONE`: 自动不确认模式，RabbitMQ 不会等待应用程序显式地返回确认信息。
  这种模式忽略了应用程序的状态，仅肯定消息是否已成功从 RabbitMQ 中删除。
  NONE 模式非常危险，因为如果您的应用程序无法处理消息，则消息仅会在服务器重启时被删除。
- `MANUAL`：手动确认模式，也称为显式确认模式，它需要应用显式地确认消息妥善处理，以便通知 RabbitMQ
  该消息已经被处理完毕，并且可以将其删除。使用手动确认模式时必须考虑如何把未处理的消息进行重新处理。手动确认模式比自动模式更加安全，能够精确地控制消费吞吐量和错误处理。
- `AUTO`: 自动确认模式，在一些场景下，我们可以选择自动确认消息的传递，每当消费者从队列中获取到一条消息时，就会立即确认收到了该消息。这种确认方式针对简单的应用场景非常实用。但是它不能精确地控制消费吞吐量和错误处理。

#### a:全局方式

`application.yml`中配置：`spring.rabbitmq.listener.simple.acknowledge-mode=manual`

然后进行测试

```java
package com.zhengqing.demo.controller;

import cn.hutool.core.date.DateTime;
import com.rabbitmq.client.Channel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Api(tags = "测试mq-ack")
@RestController
@RequestMapping("/mq")
public class AckController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @ApiOperation("手动ACK")
    @PostMapping("ack")
    public String ack() {
        String msgContent = "Hello World " + DateTime.now();
        log.info("{} [生产者] 发送消息: {}", DateTime.now(), msgContent);
        this.rabbitTemplate.convertAndSend("test_exchange", "test_ack_routing_key", msgContent);
        return "SUCCESS";
    }

    @RabbitHandler
    @RabbitListener(queues = {"test_ack_queue"})
    public void consumer(String msg, Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            log.info("----------------------------------------------");
            log.info("{} [消费者] deliveryTag:{} 接收消息: {}", DateTime.now(), deliveryTag, msg);
//            int num = 1 / 0;
            // 手动确认消息已被消费
//            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            // 丢弃消息
            channel.basicReject(deliveryTag, false);
            // 重新入队再次消费
//            channel.basicReject(deliveryTag, true);
            e.printStackTrace();
        }
    }

}
```

#### b:指定单个消费者的ack模式

```java
package com.zhengqing.demo.controller;

import cn.hutool.core.date.DateTime;
import com.rabbitmq.client.Channel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Api(tags = "测试mq-ack")
@RestController
@RequestMapping("/mq")
public class AckController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @ApiOperation("手动ACK（指定单个）")
    @PostMapping("ack2")
    public String ack2() {
        String msgContent = "Hello World " + DateTime.now();
        log.info("{} [生产者] 发送消息: {}", DateTime.now(), msgContent);
        this.rabbitTemplate.convertAndSend("test_exchange", "test_ack_manual_routing_key", msgContent);
        return "SUCCESS";
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("test_ack_manual_queue");
        // 指定ack模式
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            long deliveryTag = message.getMessageProperties().getDeliveryTag();
            log.info("----------------------------------------------");
            log.info("{} [消费者] deliveryTag:{} 接收消息: {}", DateTime.now(), deliveryTag, new String(message.getBody()));
            if (message.getMessageProperties().getHeaders().get("error") == null) {
                channel.basicAck(deliveryTag, false);
            } else {
                channel.basicReject(deliveryTag, false);
            }
        });
        return container;
    }

}
```

### 二、ACK参数说明

```
// ack正常确认
void basicAck(long deliveryTag, boolean multiple) throws IOException;

// 错误确认
//      basicReject: 只能否定一条消息
//      basicNack: 可以否定一条或者多条消息
void basicReject(long deliveryTag, boolean requeue) throws IOException;
void basicNack(long deliveryTag, boolean multiple, boolean requeue) throws IOException;

// 属性解释
//      deliveryTag：消息的唯一标识符
//      multiple：是否批量确认（如果设置为true，则表示确认所有小于等于deliveryTag的消息）
//      requeue： 是否重新将消息放回队列
//                true -- 允许消息重新排队 （业务中注意一直出现死循环处理，导致消息积压） 
//                false -- 拒绝重新排队消息，丢弃消息
```

