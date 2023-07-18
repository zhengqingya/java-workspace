### 一、前言

本文将会基于`springboot 2.4.0`通过`spring-retry`实现`服务重试`

即方法A调用方法B，若因网络不稳定或其它原因导致调用失败，可通过配置来进行一定的重试策略，以及最终超过重试次数后的回调处理。

### 二、编程

- 普通使用方式见[RetryTest.java](src/test/java/com/zhengqing/demo/RetryTest.java)
- 下面为注解使用方式

#### 1、`pom.xml`中引入依赖

```
<!-- Spring Boot AOP Starter -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>

<!-- spring-retry -->
<!-- https://mvnrepository.com/artifact/org.springframework.retry/spring-retry -->
<dependency>
    <groupId>org.springframework.retry</groupId>
    <artifactId>spring-retry</artifactId>
    <version>1.3.2</version>
</dependency>
```

#### 2、启用重试

```java

@EnableRetry // 启用重试
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
```

#### 3、测试代码

> tips: 简单小示例看看效果~

```java

@Slf4j
@RestController
@RequestMapping("")
@Api(tags = "测试重试")
public class RetryController {

    /**
     * @Retryable 参数说明
     * value：抛出指定异常才会重试
     * maxAttempts：最大重试次数，默认3次
     * backoff：重试等待策略
     * recover：执行回调方法名称，必须在当前类中 -- tips:旧版本中无此参数，好像会自动对应失败回调方法
     */
    @ApiOperation("服务重试")
    @GetMapping("retryable")
    @SneakyThrows(Exception.class)
    @Retryable(value = RetryException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 1.5), recover = "recover")
    public String retry(@RequestParam String msg) {
        log.info("test被调用,时间：" + DateTime.now());
        final int a = 3;
        int num = new SecureRandom().nextInt();
        if (num % a == 0) {
            log.info("服务调用正常！");
            return "OK";
        }
        log.info("服务调用不正常。。。");
        throw new RetryException("服务调用不正常。。。");
    }

    @Recover
    public String recover(RetryException e, String msg) {
        log.info("执行回调方法: {}", msg);
        return "FAIL";
    }

}
```

日志如下：
![](./images/README-1689662836319.png)

### 三、注意事项

`@Retryable`基于aop实现，因此需要注意失效场景，例如同一个类中调用方法

如果非要在同一个类中调用，可以通过`BeanPostProcessor在目标对象中注入代理对象` -> 解决Spring
AOP不拦截对象内部调用方法问题`^_^`

### 四、本文案例demo源码

[https://gitee.com/zhengqingya/java-workspace](https://gitee.com/zhengqingya/java-workspace)


--- 

> 今日分享语句：
> 生活教会我们的第一件事就是：没有人可以宠你一辈子，要想走得精彩又坦荡，唯有独立坚强。新的一天开始了，替未来的你抱抱现在努力奋斗的自己！

