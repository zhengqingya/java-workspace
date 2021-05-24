### 一、前言

本文基于`springboot 2.3.1.RELEASE`整合`RocketMQ`，实现简单的`发送消息`和`接收消息`。

### 二、安装RocketMQ

```shell
git clone https://gitee.com/zhengqingya/docker-compose.git
cd docker-compose/Liunx
# 运行 【注：修改 xx/rocketmq/rocketmq_broker/conf/broker.conf中配置brokerIP1为宿主机IP】
docker-compose -f docker-compose-rocketmq.yml -p rocketmq up -d
```

RocketMQ控制台地址 `http://ip地址:9002`
![在这里插入图片描述](https://img-blog.csdnimg.cn/2021052417202957.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)

### 三、SpringBoot整合RocketMQ

#### 1、`pom.xml`中引入RocketMQ依赖

```xml
<dependency>
  <groupId>org.apache.rocketmq</groupId>
  <artifactId>rocketmq-spring-boot-starter</artifactId>
  <version>2.0.3</version>
</dependency>
```

#### 2、`application.yml`中RocketMQ配置

```yml
# RocketMQ配置
rocketmq:
  name-server: 192.168.0.88:9876 # TODO 配置mq服务ip
  producer:
    group: rocketmq-producer-group
```

#### 3、定时小程序实现简单的发送消息和接收消息

```java
@Slf4j
@Component
@EnableScheduling
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
```

运行
![在这里插入图片描述](https://img-blog.csdnimg.cn/2021052417281353.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)


### 本文案例demo源码

[https://gitee.com/zhengqingya/java-workspace](https://gitee.com/zhengqingya/java-workspace)

---

> 今日分享语句：
> 这个世界上有很多事，都是当你开始认真对待以后，才会发现其中包含的乐趣，你要带着关爱而不是期待地投入生活，当你对待事物越认真，对待工作越投入，你会发现能力与乐趣接踵而来。
