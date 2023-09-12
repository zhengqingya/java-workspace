@[TOC](文章目录)

### 一、前言

Plumelog 一个简单易用的java分布式日志组件

> [https://gitee.com/plumeorg/plumelog](https://gitee.com/plumeorg/plumelog)

### 二、docker-compose一键搭建日志服务

`plumelog` + `elasticsearch` + `redis`

> tips: 详情可查看 [https://gitee.com/zhengqingya/docker-compose](https://gitee.com/zhengqingya/docker-compose)

```shell
# 准备
git clone https://gitee.com/zhengqingya/docker-compose.git
cd docker-compose/Linux/plumelog

# 运行
docker-compose -f docker-compose.yml -p plumelog up -d

# 当前目录下所有文件赋予权限(读、写、执行)  -- 解决es启动报错问题...
chmod -R 777 ./app/elasticsearch
```

- 访问地址：ip地址:8891
- 账号：admin
- 密码：admin

#### docker-compose.yml

```yml
version: "3"

# 网桥 -> 方便相互通讯
networks:
  plumelog:
    ipam:
      driver: default
      config:
        - subnet: "172.23.0.0/24"

services:
  plumelog:
    image: registry.cn-hangzhou.aliyuncs.com/zhengqing/plumelog:3.5.3
    container_name: plumelog
    restart: unless-stopped
    volumes:
      # 引用外部配置文件    ex: es,redis   (配置也可通过 -e 去修改)
      - ./app/plumelog/application.properties:/home/application.properties
    environment: # 设置环境变量,相当于docker run命令中的-e
      TZ: Asia/Shanghai
      LANG: en_US.UTF-8
      JAVA_OPTS: "-XX:+UseG1GC -Dspring.config.location=/home/application.properties,classpath:application.properties,"
      plumelog.queue.redis.redisHost: 172.23.0.33:6379
      plumelog.redis.redisHost: 172.23.0.33:6379
      plumelog.es.esHosts: 172.23.0.22:9200
    ports:
      - "8891:8891"
    depends_on:
      - elasticsearch
      - redis
    networks:
      plumelog:
        ipv4_address: 172.23.0.11

  elasticsearch:
    image: registry.cn-hangzhou.aliyuncs.com/zhengqing/elasticsearch:7.14.1      # 原镜像`elasticsearch:7.14.1`
    container_name: plumelog_elasticsearch         # 容器名为'elk_elasticsearch'
    restart: unless-stopped                   # 指定容器退出后的重启策略为始终重启，但是不考虑在Docker守护进程启动时就已经停止了的容器
    volumes:                                  # 数据卷挂载路径设置,将本机目录映射到容器目录
      - "./app/elasticsearch/data:/usr/share/elasticsearch/data"
      - "./app/elasticsearch/logs:/usr/share/elasticsearch/logs"
      - "./app/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml"
    #      - "./app/elasticsearch/config/jvm.options:/usr/share/elasticsearch/config/jvm.options"
    environment:                              # 设置环境变量,相当于docker run命令中的-e
      TZ: Asia/Shanghai
      LANG: en_US.UTF-8
      discovery.type: single-node
      ES_JAVA_OPTS: "-Xmx512m -Xms512m"
      ELASTIC_PASSWORD: "123456" # elastic账号密码
    ports:
      - "9200:9200"
      - "9300:9300"
    networks:
      plumelog:
        ipv4_address: 172.23.0.22

  redis:
    image: registry.cn-hangzhou.aliyuncs.com/zhengqing/redis:7.0.5                    # 镜像'redis:7.0.5'
    container_name: plumelog_redis                                                             # 容器名为'redis'
    restart: unless-stopped                                                                   # 指定容器退出后的重启策略为始终重启，但是不考虑在Docker守护进程启动时就已经停止了的容器
    command: redis-server /etc/redis/redis.conf --requirepass 123456 --appendonly no # 启动redis服务并添加密码为：123456,默认不开启redis-aof方式持久化配置
    #    command: redis-server --requirepass 123456 --appendonly yes # 启动redis服务并添加密码为：123456,并开启redis持久化配置
    environment:                        # 设置环境变量,相当于docker run命令中的-e
      TZ: Asia/Shanghai
      LANG: en_US.UTF-8
    volumes:                            # 数据卷挂载路径设置,将本机目录映射到容器目录
      - "./app/redis/data:/data"
      - "./app/redis/config/redis.conf:/etc/redis/redis.conf"  # `redis.conf`文件内容`http://download.redis.io/redis-stable/redis.conf`
    ports:                              # 映射端口
      - "6379:6379"
    networks:
      plumelog:
        ipv4_address: 172.23.0.33
```

### 三、SpringBoot整合Plumelog

#### 1、`pom.xml`

```xml
<dependency>
    <groupId>com.plumelog</groupId>
    <artifactId>plumelog-logback</artifactId>
    <version>3.5.3</version>
</dependency>
```

#### 2、`application.yml`

```yml
spring:
  profiles:
    active: dev

# 配置日志地址
logging:
  config: classpath:logback-spring.xml

plumelog:
  appName: plumelog_demo
  # 需要和plumelog服务端的redis配置一致
  redisHost: 127.0.0.1:6379
  redisAuth: 123456
  redisDb: 10
```

#### 3、`logback-spring.xml`

```xml
<!-- 环境配置 -->
<springProperty scope="context" name="plumelog.appName" source="plumelog.appName"/>
<springProperty scope="context" name="plumelog.redisHost" source="plumelog.redisHost"/>
<springProperty scope="context" name="plumelog.redisAuth" source="plumelog.redisAuth"/>
<springProperty scope="context" name="plumelog.redisDb" source="plumelog.redisDb"/>
<springProperty scope="context" name="plumelog.env" source="spring.profiles.active"/>
<!-- plumelog -->
<appender name="plumelog" class="com.plumelog.logback.appender.RedisAppender">
    <appName>${plumelog.appName}</appName>
    <redisHost>${plumelog.redisHost}</redisHost>
    <redisAuth>${plumelog.redisAuth}</redisAuth>
    <redisDb>${plumelog.redisDb}</redisDb>
    <env>${plumelog.env}</env>
</appender>
<root level="DEBUG">
    <appender-ref ref="plumelog"/>
</root>
```

#### 4、启动项目记录日志

访问plumelog的ui界面查看日志

![](./images/20230912141309354.png)

### 四、本文案例demo源码

[https://gitee.com/zhengqingya/java-workspace](https://gitee.com/zhengqingya/java-workspace)


---

> 今日分享语句：
> 我一定会加油，直追我的目标，有些该放弃的我会放弃，再也不会幻想了，未来的日子，我想你一样，坚强，达到我想要的目标！
