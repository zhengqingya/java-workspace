### 一、前言

#### Kafka是什么？

1. Kafka是一种`高吞吐量`的`分布式`发布订阅`消息系统`，可以处理消费者在网站中的所有动作流数据。
2. 目的：通过Hadoop的并行加载机制来统一线上和离线的消息处理，也是为了通过集群来提供实时的消息。

#### 环境

1. springboot
2. idea
3. docker-compose
4. zookeeper
5. kafka
6. kafka-manager


### 二、docker-compose安装kafka

#### 1. docker-compose-kafka.yml

```yml
version: '3'
services:
  zookepper:
    image: wurstmeister/zookeeper                    # 原镜像`wurstmeister/zookeeper`
    container_name: zookeeper_server                 # 容器名为'zookeeper_server'
    restart: always                                  # 指定容器退出后的重启策略为始终重启
    volumes:                                         # 数据卷挂载路径设置,将本机目录映射到容器目录
      - "/etc/localtime:/etc/localtime"
    ports:                                           # 映射端口
      - "2181:2181"

  kafka:
    image: wurstmeister/kafka                                # 原镜像`wurstmeister/kafka`
    container_name: kafka_server                             # 容器名为'kafka_server'
    restart: always                                          # 指定容器退出后的重启策略为始终重启
    volumes:                                                 # 数据卷挂载路径设置,将本机目录映射到容器目录
      - "/etc/localtime:/etc/localtime"
    environment:                        # 设置环境变量,相当于docker run命令中的-e
      KAFKA_ADVERTISED_HOST_NAME: www.zhengqingya.com  # TODO 本机IP
      KAFKA_ADVERTISED_PORT: 9092                      # 端口
      KAFKA_BROKER_ID: 0                # 在kafka集群中，每个kafka都有一个BROKER_ID来区分自己
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://www.zhengqingya.com:9092 # TODO 将kafka的地址端口注册给zookeeper
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092        # 配置kafka的监听端口
      KAFKA_ZOOKEEPER_CONNECT: www.zhengqingya.com:2181 # TODO zookeeper地址
      KAFKA_CREATE_TOPICS: "hello_world"
    ports:                              # 映射端口
      - "9092:9092"
    depends_on:                         # 解决容器依赖启动先后问题
      - zookepper

  kafka-manager:
    image: sheepkiller/kafka-manager                         # 原镜像`sheepkiller/kafka-manager`
    container_name: kafka-manager                            # 容器名为'kafka-manager'
    restart: always                                          # 指定容器退出后的重启策略为始终重启
    environment:                        # 设置环境变量,相当于docker run命令中的-e
      ZK_HOSTS: www.zhengqingya.com:2181  # TODO zookeeper地址
      APPLICATION_SECRET: zhengqing
      KAFKA_MANAGER_AUTH_ENABLED: "true"  # 开启kafka-manager权限校验
      KAFKA_MANAGER_USERNAME: admin       # 登陆账户
      KAFKA_MANAGER_PASSWORD: 123456      # 登陆密码
    ports:                              # 映射端口
      - "9001:9000"
    depends_on:                         # 解决容器依赖启动先后问题
      - kafka
```


#### 2. 运行

```shell
docker-compose -f docker-compose-kafka.yml -p kafka up -d
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200421234825103.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200421234914526.png)

#### 3. `kafka-manager`(kafka集群管理工具)

访问[`ip:9001`](http://www.zhengqingya.com:9001/) 

> 温馨小提示：下面只是简单使用，更多可自行查询资料了解

###### ① 新建Cluster: 点击`Cluster` -> `Add Cluster`

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200421221632917.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200421235613333.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)
Save保存时出现如下提示至少为2，修改一下默认值为2即可~
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200421222702691.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)
###### ② 查看topic

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200421235705960.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020042123572469.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200422000051554.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)

### 三、SpringBoot整合Kafka

#### ① `pom.xml`中引入依赖

```java
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

#### ② `application.yml`中配置kafka

```yml
spring:
  # ======================== ↓↓↓↓↓↓ kafka相关配置 ↓↓↓↓↓↓ ===============================
  kafka:
    bootstrap-servers: www.zhengqingya.com:9092 # 指定kafka server地址，集群（多个逗号分隔）
    producer:
      # 指定消息key和消息体的编解码方式
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
      # 写入失败时，重试次数。当leader节点失效，一个repli节点会替代成为leader节点，此时可能出现写入失败，
      # 当retris为0时，produce不会重复。retirs重发，此时repli节点完全成为leader节点，不会产生消息丢失。
      retries: 0
      # 每次批量发送消息的数量,produce积累到一定数据，一次发送
      batch-size: 16384
      # produce积累数据一次发送，缓存大小达到buffer.memory就发送数据
      buffer-memory: 33554432
    consumer:
      group-id: default_consumer_group # 指定默认消费者 群组ID
      enable-auto-commit: true
      auto-commit-interval: 1000
      # procedure要求leader在考虑完成请求之前收到的确认数，用于控制发送记录在服务端的持久化，其值可以为如下：
      # acks = 0 如果设置为零，则生产者将不会等待来自服务器的任何确认，该记录将立即添加到套接字缓冲区并视为已发送。在这种情况下，无法保证服务器已收到记录，并且重试配置将不会生效（因为客户端通常不会知道任何故障），为每条记录返回的偏移量始终设置为-1。
      # acks = 1 这意味着leader会将记录写入其本地日志，但无需等待所有副本服务器的完全确认即可做出回应，在这种情况下，如果leader在确认记录后立即失败，但在将数据复制到所有的副本服务器之前，则记录将会丢失。
      # acks = all 这意味着leader将等待完整的同步副本集以确认记录，这保证了只要至少一个同步副本服务器仍然存活，记录就不会丢失，这是最强有力的保证，这相当于acks = -1的设置。
      # 可以设置的值为：all, -1, 0, 1
      acks: 1
      # 指定消息key和消息体的编解码方式
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
```

#### ③ 生产者 - 发送消息

```java
@RestController
@RequestMapping("/api/")
public class Producer {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @RequestMapping("send")
    public String send(String msg) {
        kafkaTemplate.send("hello", msg);
        return "SUCCESS";
    }
}
```

#### ④ 消费者 - 接收消息

```java
@Slf4j
@Component
public class Consumer {
    @KafkaListener(topics = "hello")
    public void listen(ConsumerRecord<?, ?> record) {
        log.info("topic: " + record.topic() + "  <|============|>  消息内容：" + record.value());
    }
}
```


### 四、测试

① 调用接口发送消息：[http://127.0.0.1/api/send?msg=hello,kafka](http://127.0.0.1/api/send?msg=hello,kafka)

② 查看控制台打印日志信息：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200421233248635.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)


---


### 本文案例demo源码

[https://gitee.com/zhengqingya/java-workspace](https://gitee.com/zhengqingya/java-workspace)

