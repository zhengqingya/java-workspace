# Kafka-Eagle 监控

Kafka-Eagle 框架可以监控 Kafka 集群的整体运行情况，在生产环境中经常使用。

> tips: 此方式未实践过，docker方式可见 https://gitee.com/zhengqingya/docker-compose

### 一、配置 Kafka

Kafka-Eagle 的安装依赖于 MySQL，MySQL 主要用来存储可视化展示的数据。

```
vim bin/kafka-server-start.sh
```

修改如下参数值：

```
if [ "x$KAFKA_HEAP_OPTS" = "x" ]; then
	export KAFKA_HEAP_OPTS="-Xmx1G -Xms1G"
fi
```

为

```
if [ "x$KAFKA_HEAP_OPTS" = "x" ]; then
	export KAFKA_HEAP_OPTS="-server -Xms2G -Xmx2G -XX:PermSize=128m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:ParallelGCThreads=8 -XX:ConcGCThreads=5 -XX:InitiatingHeapOccupancyPercent=70"
	export JMX_PORT="9999"
	#export KAFKA_HEAP_OPTS="-Xmx1G -Xms1G"
fi
```

修改之后在启动 Kafka

### 二、安装 Kafka-Eagle

下载解压 https://www.kafka-eagle.org/

修改配置文件 `efak/conf/system-config.properties`

```
######################################

# multi zookeeper & kafka cluster list

# Settings prefixed with 'kafka.eagle.' will be deprecated, use 'efak.'

instead
######################################
efak.zk.cluster.alias=cluster1
cluster1.zk.list=zookepper1:2181,zookepper2:2181,zookepper3:2181/kafka
######################################

# zookeeper enable acl

######################################
cluster1.zk.acl.enable=false
cluster1.zk.acl.schema=digest
cluster1.zk.acl.username=test
cluster1.zk.acl.password=test123
######################################

# broker size online list

######################################
cluster1.efak.broker.size=20
######################################

# zk client thread limit

######################################
kafka.zk.limit.size=32
######################################

# EFAK webui port

######################################
efak.webui.port=8048
######################################

# kafka jmx acl and ssl authenticate

######################################
cluster1.efak.jmx.acl=false
cluster1.efak.jmx.user=keadmin
cluster1.efak.jmx.password=keadmin123
cluster1.efak.jmx.ssl=false
cluster1.efak.jmx.truststore.location=/data/ssl/certificates/kafka.truststore
cluster1.efak.jmx.truststore.password=ke123456
######################################

# kafka offset storage

######################################

# offset 保存在 kafka

cluster1.efak.offset.storage=kafka

######################################

# kafka jmx uri

######################################
cluster1.efak.jmx.uri=service:jmx:rmi:///jndi/rmi://%s/jmxrmi
######################################

# kafka metrics, 15 days by default

######################################
efak.metrics.charts=true
efak.metrics.retain=15
######################################

# kafka sql topic records max

######################################
efak.sql.topic.records.max=5000
efak.sql.topic.preview.records.max=10
######################################

# delete kafka topic token

######################################
efak.topic.token=keadmin
######################################

# kafka sasl authenticate

######################################
cluster1.efak.sasl.enable=false
cluster1.efak.sasl.protocol=SASL_PLAINTEXT
cluster1.efak.sasl.mechanism=SCRAM-SHA-256
cluster1.efak.sasl.jaas.config=org.apache.kafka.common.security.scram.ScramL
oginModule required username="kafka" password="kafka-eagle";
cluster1.efak.sasl.client.id=
cluster1.efak.blacklist.topics=
cluster1.efak.sasl.cgroup.enable=false
cluster1.efak.sasl.cgroup.topics=
cluster2.efak.sasl.enable=false
cluster2.efak.sasl.protocol=SASL_PLAINTEXT
cluster2.efak.sasl.mechanism=PLAIN
cluster2.efak.sasl.jaas.config=org.apache.kafka.common.security.plain.PlainL
oginModule required username="kafka" password="kafka-eagle";
cluster2.efak.sasl.client.id=
cluster2.efak.blacklist.topics=
cluster2.efak.sasl.cgroup.enable=false
cluster2.efak.sasl.cgroup.topics=
######################################

# kafka ssl authenticate

######################################
cluster3.efak.ssl.enable=false
cluster3.efak.ssl.protocol=SSL
cluster3.efak.ssl.truststore.location=
cluster3.efak.ssl.truststore.password=
cluster3.efak.ssl.keystore.location=
cluster3.efak.ssl.keystore.password=
cluster3.efak.ssl.key.password=
cluster3.efak.ssl.endpoint.identification.algorithm=https
cluster3.efak.blacklist.topics=
cluster3.efak.ssl.cgroup.enable=false
cluster3.efak.ssl.cgroup.topics=
######################################

# kafka sqlite jdbc driver address

######################################

# kafka mysql jdbc driver address

######################################
# 配置 mysql 连接
efak.driver=com.mysql.jdbc.Driver
efak.url=jdbc:mysql://127.0.0.1:3306/ke?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull
efak.username=root
efak.password=root
######################################
```

之后运行查看效果...
