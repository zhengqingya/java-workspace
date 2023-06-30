# Kafka 命令行操作

### 一、主题命令行操作

```shell
# 查看参数
docker exec -it kafka-1 /opt/bitnami/kafka/bin/kafka-topics.sh
```

| 参数                                                | 描述                         |
|---------------------------------------------------|----------------------------|
| -–bootstrap-server <String: server toconnect to>  | 连接的 Kafka Broker 主机名称和端口号。 |
| -–topic <String: topic>                           | 操作的 topic 名称。              |
| -–create                                          | 创建主题。                      |
| -–delete                                          | 删除主题。                      |
| -–alter                                           | 修改主题。                      |
| -–list                                            | 查看所有主题。                    |
| -–describe                                        | 查看主题详细描述。                  |
| -–partitions <Integer: # of partitions>           | 设置分区数。                     |
| -–replication-factor<Integer: replication factor> | 设置分区副本。                    |
| -–config <String: name=value>                     | 更新系统默认的配置。                 |

```shell
# 查看所有topic -- 其中--bootstrap-server可以连接集群，用英文逗号分隔，一般连接两三个节点，保证其中某个节点挂掉的情况下，其它节点能够正常拿到数据
# docker exec -it kafka-1 /opt/bitnami/kafka/bin/kafka-topics.sh --bootstrap-server kafka-1:9092,kafka-2:9093,kafka-3:9094 --list
docker exec -it kafka-1 /opt/bitnami/kafka/bin/kafka-topics.sh --bootstrap-server kafka-1:9092 --list
# 创建主题 `my-topic`
docker exec -it kafka-1 /opt/bitnami/kafka/bin/kafka-topics.sh --create --bootstrap-server kafka-1:9092 --topic my-topic --partitions 3 --replication-factor 1
# 查看主题详情
docker exec -it kafka-1 /opt/bitnami/kafka/bin/kafka-topics.sh --bootstrap-server kafka-1:9092 --describe --topic my-topic
# 修改分区数 3->6（注意：分区数只能增加，不能减少）
docker exec -it kafka-1 /opt/bitnami/kafka/bin/kafka-topics.sh --bootstrap-server kafka-1:9092 --alter --topic my-topic --partitions 6
# 删除主题 -- tips: 确保停止对要删除的主题进行任何生产和消费操作。这可以通过停止相关的生产者和消费者应用程序来实现。（windows上如果配置了持久化数据，删除主题会出现节点无法连接的问题...）
docker exec -it kafka-1 /opt/bitnami/kafka/bin/kafka-topics.sh --bootstrap-server kafka-1:9092 --delete --topic my-topic
```

---

### 二、生产者命令行操作

```shell
# 查看参数
docker exec -it kafka-1 /opt/bitnami/kafka/bin/kafka-console-producer.sh
```

| 参数                                               | 描述                         |
|--------------------------------------------------|----------------------------|
| -–bootstrap-server <String: server toconnect to> | 连接的 Kafka Broker 主机名称和端口号。 |
| -–topic <String: topic>                          | 操作的 topic 名称。              |

```shell
# 发送消息
docker exec -it kafka-1 /opt/bitnami/kafka/bin/kafka-console-producer.sh --bootstrap-server kafka-1:9092 --topic my-topic
```

### 三、消费者命令行操作

```
查看参数
docker exec -it kafka-1 /opt/bitnami/kafka/bin/kafka-console-consumer.sh --bootstrap-server kafka-1:9092
```

| 参数                                               | 描述                         |
|--------------------------------------------------|----------------------------|
| -–bootstrap-server <String: server toconnect to> | 连接的 Kafka Broker 主机名称和端口号。 |
| -–topic <String: topic>                          | 操作的 topic 名称。              |
| -–from-beginning                                 | 从头开始消费。                    |
| -–group <String: consumer group id>              | 指定消费者组名称。                  |

```shell
# 读取消息
docker exec -it kafka-1 /opt/bitnami/kafka/bin/kafka-console-consumer.sh --bootstrap-server kafka-1:9092 --topic my-topic
# 读取历史数据
docker exec -it kafka-1 /opt/bitnami/kafka/bin/kafka-console-consumer.sh --bootstrap-server kafka-1:9092 --from-beginning --topic my-topic
```
