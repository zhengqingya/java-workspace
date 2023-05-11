@[TOC](文章目录)

### 一、前言

本文通过以下环境整合`Seata` + `多数据源` + `sharding-jdbc`

1. spring-boot 2.7.0
2. spring-cloud 2021.0.2
3. spring-cloud-alibaba 2021.0.1.0
4. sharding-jdbc 4.1.1
5. seata-server 1.5.2
6. dynamic-datasource 3.3.2

### 二、docker-compose一键部署Seata

见 https://gitee.com/zhengqingya/docker-compose

### 三、项目整合

> tips: 详情见文末提供的源码demo

#### 1、引入依赖

```
<dependencies>
    <!-- sharding-jdbc -->
    <!-- https://mvnrepository.com/artifact/org.apache.shardingsphere/sharding-jdbc-spring-boot-starter -->
    <dependency>
        <groupId>org.apache.shardingsphere</groupId>
        <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
        <version>4.1.1</version>
    </dependency>
    <!-- 整合seata -->
    <dependency>
        <groupId>org.apache.shardingsphere</groupId>
        <artifactId>sharding-transaction-base-seata-at</artifactId>
        <version>4.1.1</version>
    </dependency>

    <!-- 动态数据源 -->
    <!-- https://mvnrepository.com/artifact/com.baomidou/dynamic-datasource-spring-boot-starter -->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
        <version>3.3.2</version>
    </dependency>

    <!-- seata -->
    <!-- 最外层父pom.xml中统一管理seata版本 （全局修改版本为1.5.2） -->
    <!-- https://mvnrepository.com/artifact/com.alibaba.cloud/spring-cloud-starter-alibaba-seata -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
    </dependency>
    <!-- https://mvnrepository.com/artifact/io.seata/seata-spring-boot-starter -->
    <dependency>
        <groupId>io.seata</groupId>
        <artifactId>seata-spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```

#### 2、增加配置文件

```yml
spring:
  # 多数据源配置 可参考 https://gitee.com/baomidou/dynamic-datasource-spring-boot-starter
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    dynamic:
      primary: master # 设置默认的数据源或者数据源组,默认值即为master
      strict: false   # 设置严格模式,默认false不启动. 启动后在未匹配到指定数据源时候会抛出异常,不启动则使用默认数据源.
      datasource:
        master:
          url: jdbc:mysql://127.0.0.1:3306/demo?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&useSSL=false # MySQL在高版本需要指明是否进行SSL连接 解决则加上 &useSSL=false
          username: root
          password: root
          driver-class-name: com.mysql.cj.jdbc.Driver
        db-test:
          url: jdbc:mysql://127.0.0.1:3306/demo-bak?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&useSSL=false # MySQL在高版本需要指明是否进行SSL连接 解决则加上 &useSSL=false
          username: root
          password: root
          driver-class-name: com.mysql.cj.jdbc.Driver

      # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭
      seata: true
      seata-mode: at


  # sharding-jdbc配置
  shardingsphere:
    # 是否开启SQL显示
    props:
      sql:
        show: false
    # ====================== ↓↓↓↓↓↓ 数据源配置 ↓↓↓↓↓↓ ======================
    datasource:
      names: ds-master-0,ds-slave-0-1,ds-slave-0-2,ds-master-1,ds-slave-1-1,ds-slave-1-2
      # ====================== ↓↓↓↓↓↓ 配置第1个主从库 ↓↓↓↓↓↓ ======================
      # 主库1
      ds-master-0:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://127.0.0.1:3306/ds0?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&useSSL=false # MySQL在高版本需要指明是否进行SSL连接 解决则加上 &useSSL=false
        username: root
        password: root
      # 主库1-从库1
      ds-slave-0-1:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://127.0.0.1:3306/ds0?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&useSSL=false # MySQL在高版本需要指明是否进行SSL连接 解决则加上 &useSSL=false
        username: root
        password: root
      # 主库1-从库2
      ds-slave-0-2:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://127.0.0.1:3306/ds0?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&useSSL=false # MySQL在高版本需要指明是否进行SSL连接 解决则加上 &useSSL=false
        username: root
        password: root
      # ====================== ↓↓↓↓↓↓ 配置第2个主从库 ↓↓↓↓↓↓ ======================
      # 主库2
      ds-master-1:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://127.0.0.1:3306/ds1?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&useSSL=false # MySQL在高版本需要指明是否进行SSL连接 解决则加上 &useSSL=false
        username: root
        password: root
      # 主库2-从库1
      ds-slave-1-1:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://127.0.0.1:3306/ds1?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&useSSL=false # MySQL在高版本需要指明是否进行SSL连接 解决则加上 &useSSL=false
        username: root
        password: root
      # 主库2-从库2
      ds-slave-1-2:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://127.0.0.1:3306/ds1?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&useSSL=false # MySQL在高版本需要指明是否进行SSL连接 解决则加上 &useSSL=false
        username: root
        password: root

    sharding:
      # ====================== ↓↓↓↓↓↓ 读写分离配置 ↓↓↓↓↓↓ ======================
      master-slave-rules:
        ds-master-0:
          # 主库
          masterDataSourceName: ds-master-0
          # 从库
          slaveDataSourceNames:
            - ds-slave-0-1
            - ds-slave-0-2
          # 从库查询数据的负载均衡算法 目前有2种算法 round_robin（轮询）和 random（随机）
          # 算法接口 org.apache.shardingsphere.spi.masterslave.MasterSlaveLoadBalanceAlgorithm
          # 实现类 RandomMasterSlaveLoadBalanceAlgorithm 和 RoundRobinMasterSlaveLoadBalanceAlgorithm
          loadBalanceAlgorithmType: ROUND_ROBIN
        ds-master-1:
          masterDataSourceName: ds-master-1
          slaveDataSourceNames:
            - ds-slave-1-1
            - ds-slave-1-2
          loadBalanceAlgorithmType: ROUND_ROBIN

      # ====================== ↓↓↓↓↓↓ 分库分表配置 ↓↓↓↓↓↓ ======================
      tables:
        t_user:
          actual-data-nodes: ds-master-$->{0..1}.t_user$->{0..1}
          database-strategy:
            complex:
              sharding-columns: create_time
              algorithm-class-name: com.zhengqing.common.db.config.sharding.user.complex.MyDbComplexKeysShardingAlgorithm
          table-strategy:
            complex:
              sharding-columns: user_id
              algorithm-class-name: com.zhengqing.common.db.config.sharding.user.complex.MyTableComplexKeysShardingAlgorithm
        t_order:
          actual-data-nodes: ds-master-$->{0..1}.t_order$->{0..1}
          database-strategy:
            complex:
              sharding-columns: pay_time
              algorithm-class-name: com.zhengqing.common.db.config.sharding.order.complex.MyDbComplexKeysShardingAlgorithm
          table-strategy:
            complex:
              sharding-columns: user_id
              algorithm-class-name: com.zhengqing.common.db.config.sharding.order.complex.MyTableComplexKeysShardingAlgorithm

# seata配置
seata:
  # 是否开启seata
  enabled: true
  # Seata 应用编号，默认为 ${spring.application.name}
  application-id: ${spring.application.name}
  # Seata 事务组编号，用于 TC 集群名
  tx-service-group: test-tx-group
  # 是否开启数据源代理
  enable-auto-data-source-proxy: false
  data-source-proxy-mode: AT
  # 服务配置项
  service:
    # 虚拟组和分组的映射
    vgroup-mapping:
      test-tx-group: default
    # 分组和 Seata 服务的映射
    grouplist:
      default: 127.0.0.1:8091
  config:
    type: nacos
    nacos:
      serverAddr: ${spring.cloud.nacos.config.server-addr}
      group: SEATA_GROUP
      namespace: ${spring.cloud.nacos.config.namespace}
      username: ${spring.cloud.nacos.config.username}
      password: ${spring.cloud.nacos.config.password}
      dataId: seata-server.properties
  registry:
    type: nacos
    nacos:
      application: seata-server
      server-addr: ${spring.cloud.nacos.config.server-addr}
      group: SEATA_GROUP
      namespace: ${spring.cloud.nacos.config.namespace}
      username: ${spring.cloud.nacos.config.username}
      password: ${spring.cloud.nacos.config.password}
```

#### 3、数据源配置

```java
package com.zhengqing.common.db.config.dynamic;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.provider.AbstractDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import org.apache.shardingsphere.shardingjdbc.jdbc.adapter.AbstractDataSourceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Map;

/**
 * <p> sharding-jdbc集成动态数据源配置 </p>
 *
 * @author zhengqingya
 * @description 使用 {@link com.baomidou.dynamic.datasource.annotation.DS} 注解，切换数据源
 * ex: 切换为sharding-jdbc数据源 => @DS(DataSourceConfig.SHARDING_DATA_SOURCE_NAME)
 * @date 2021/11/2 10:13
 */
@Configuration
@AutoConfigureBefore({DynamicDataSourceAutoConfiguration.class, SpringBootConfiguration.class})
public class DataSourceConfig {
    /**
     * 分表数据源名称
     */
    public static final String SHARDING_DATA_SOURCE_NAME = "sharding-data-source";
    /**
     * 动态数据源配置项
     */
    @Autowired
    private DynamicDataSourceProperties properties;

    /**
     * sharding-jdbc有四种数据源，需要根据业务注入不同的数据源
     * <p>
     * 1.未使用分片, 脱敏的名称(默认): shardingDataSource;
     * 2.主从数据源: masterSlaveDataSource;
     * 3.脱敏数据源：encryptDataSource;
     * 4.影子数据源：shadowDataSource
     */
    @Lazy
    @Resource(name = "shardingDataSource")
    private AbstractDataSourceAdapter shardingDataSource;

    @Bean
    public DynamicDataSourceProvider dynamicDataSourceProvider() {
        Map<String, DataSourceProperty> datasourceMap = this.properties.getDatasource();
        return new AbstractDataSourceProvider() {
            @Override
            public Map<String, DataSource> loadDataSources() {
                Map<String, DataSource> dataSourceMap = this.createDataSourceMap(datasourceMap);
                // 将 shardingjdbc 管理的数据源也交给动态数据源管理
                dataSourceMap.put(SHARDING_DATA_SOURCE_NAME, DataSourceConfig.this.shardingDataSource);
                return dataSourceMap;
            }
        };
    }

    /**
     * 将动态数据源设置为首选的
     * 当spring存在多个数据源时, 自动注入的是首选的对象
     * 设置为主要的数据源之后，就可以支持sharding-jdbc原生的配置方式了
     */
    @Primary
    @Bean
    public DataSource dataSource(DynamicDataSourceProvider dynamicDataSourceProvider) {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.setPrimary(this.properties.getPrimary());
        dataSource.setStrict(this.properties.getStrict());
        dataSource.setStrategy(this.properties.getStrategy());
        dataSource.setProvider(dynamicDataSourceProvider);
        dataSource.setP6spy(this.properties.getP6spy());
        dataSource.setSeata(this.properties.getSeata());
        return dataSource;
    }

}
```

#### 4、各个微服务下的resources目录下准备`seata.conf`

eg:

```
client {
    application.id = user-server
    transaction.service.group = test-tx-group
}
```

### 四、编写业务代码进行测试

> 使用：在对应的业务方法上加上相应的注解即可...

#### 1、分库分表

##### a:父服务声明（主业务调用方）

```
@GlobalTransactional(rollbackFor = Exception.class)
@ShardingTransactionType(TransactionType.BASE)
```

##### b:子服务声明

```
@Transactional(rollbackFor = Exception.class)
@ShardingTransactionType(TransactionType.BASE)
```

#### 2、多数据源

单库单表

```
@GlobalTransactional(rollbackFor = Exception.class)
@DS("db-test")
```

### 五、源码案例Demo

https://gitee.com/zhengqingya/java-workspace

--- 

> 今日分享语句：
> 耐心和恒心是学习的良药，坚持不懈是成功的关键。

