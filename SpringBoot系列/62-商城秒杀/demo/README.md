# 秒杀

> 参考 https://gitee.com/52itstyle/spring-boot-seckill 学习后的简化版demo

### 一、环境准备

> tips: 可参考 https://gitee.com/zhengqingya/docker-compose 一键部署中间件

#### 1、MySQL5.7

配置 `application.yml` 数据库连接参数

```yml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/demo?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&useSSL=false
    username: root
    password: root
    platform: mysql
    driver-class-name: com.mysql.jdbc.Driver
```

#### 2、Redis7.0.5

配置 `application.yml` redis连接参数

```yml
spring:
  redis:
    # Redis数据库索引（默认为0）
    database: 0
    # Redis服务器地址
    host: 127.0.0.1
    # Redis服务器连接端口
    port: 6379
    timeout: 6000
    # Redis服务器连接密码（默认为空）
    password: 123456
    jedis:
      pool:
        max-active: 100   # 连接池最大连接数（使用负值表示没有限制）  -- redis服务端最大连接数查看 `config get maxclients`；客户端单机最大连接数 * 集群数 不能超过服务端最大连接数
        max-wait: -1      # 连接池最大阻塞等待时间（使用负值表示没有限制）
        max-idle: 10      # 连接池中的最大空闲连接
        min-idle: 5       # 连接池中的最小空闲连接
```

### 二、测试

启动项目，访问 http://127.0.0.1/doc.html#/home

![](images/api-doc.png)

Controller 见 `com.zhengqing.demo.api.SeckillController.seckill`

