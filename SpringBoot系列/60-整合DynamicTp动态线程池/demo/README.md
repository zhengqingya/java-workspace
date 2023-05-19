# DynamicTp

🔥🔥🔥轻量级动态线程池，内置监控告警功能，集成三方中间件线程池管理，基于主流配置中心（已支持Nacos、Apollo，Zookeeper、Consul、Etcd，可通过SPI自定义实现）。

- https://gitee.com/dromara/dynamic-tp
- https://github.com/dromara/dynamic-tp

### 简单使用

#### 1、引入依赖

```xml
<!-- DynamicTp 基于配置中心的轻量级动态线程池 -->
<dependency>
    <groupId>org.dromara.dynamictp</groupId>
    <artifactId>dynamic-tp-spring-cloud-starter-nacos</artifactId>
    <version>1.1.3</version>
</dependency>
```

#### 2、nacos新增配置`demo-dtp-dev.yml` Group为`test`

> tips: 刚开始测试动态更新配置时将线程数从1调到3 即线程名最大为`[test-3]` 然后再将线程调为1，看见线程名还是`[test-3]`，
> 还以为只能升不能降，后面发现是自己傻了，哈哈哈，还是只有一个线程在跑，只是名称一直是`[test-3]`而已...

```yml
spring:
  # 动态线程池
  dynamic:
    tp:
      executors:
        - threadPoolName: dtpExecutorTest
          threadPoolAliasName: 测试线程池            # 线程池别名
          corePoolSize: 5
          maximumPoolSize: 10
          queueCapacity: 200
          keepAliveTime: 50
          allowCoreThreadTimeOut: false                  # 是否允许核心线程池超时
          threadNamePrefix: test                         # 线程名前缀
```

#### 3、nacos动态加载配置文件

eg: 本地配置`bootstrap.yml`

```yml
spring:
  cloud:
    nacos:
      config:
        extension-configs:
          - data-id: demo-dtp-dev.yml
            group: test
            refresh: true
```

#### 4、启动类添加注解`@EnableDynamicTp`

#### 5、测试

```java
package com.zhengqing.demo.api;


import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.dromara.dynamictp.core.DtpRegistry;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/api")
public class TestController {

    // 使用方式1
    @Resource
    private ThreadPoolExecutor dtpExecutorTest;

    @GetMapping("test")
    @ApiOperation("test")
    public Object test() {
        log.info("time: {}", DateUtil.date());
//        System.out.println(DtpRegistry.listAllExecutors());
        // 使用方式2
//        DtpExecutor dtpExecutorTest = DtpRegistry.getDtpExecutor("dtpExecutorTest");
        for (int i = 0; i < 100; i++) {
            this.dtpExecutorTest.execute(() -> {
                log.info("dynamic-tp-test");
            });
        }
        return DtpRegistry.listAllExecutorNames();
    }

}
```
