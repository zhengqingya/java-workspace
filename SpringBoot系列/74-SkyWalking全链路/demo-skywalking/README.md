# SkyWalking

- 官网：https://skywalking.apache.org/
- github：https://github.com/apache/skywalking
- 文档：https://skywalking.apache.org/docs/

### SkyWalking 原生探针

直接将数据（通过 gRPC）打给 SkyWalking OAP。

#### 1、JVM 启动参数

下载并解压 `skywalking-agent` https://skywalking.apache.org/downloads/
> eg: https://dlcdn.apache.org/skywalking/java-agent/9.6.0/apache-skywalking-java-agent-9.6.0.tgz

```shell
-javaagent:/data/skywalking-agent/skywalking-agent.jar
-Dskywalking.agent.service_name=demo-skywalking
-Dskywalking.collector.backend_service=127.0.0.1:11800
```

#### 2、日志监控 - logback

https://skywalking.apache.org/docs/skywalking-java/v9.6.0/en/setup/service-agent/java-agent/application-toolkit-logback-1.x/

##### 实现说明

SkyWalking 日志监控的实现比较简单，核心分为 3 步：

1. 在 [`pom.xml`](./pom.xml) 中引入 `apm-toolkit-logback-1.x`
2. 在 [`logback-spring.xml`](src/main/resources/logback-spring.xml) 中配置 SkyWalking 的 `%tid` 和 `%sw_ctx` 占位符，并增加 `GRPCLogClientAppender`
3. 应用通过 `-javaagent` 方式接入 SkyWalking agent 后，业务日志会在控制台打印 trace 信息，同时通过 gRPC 上报到 SkyWalking OAP

##### 日志格式对比

当前项目在 `logback-spring.xml` 中同时输出了 `%tid` 和 `%sw_ctx`，便于观察 SkyWalking 日志上下文。

| 格式 | 含义 | 包含信息 | 示例 | 适用场景 |
| --- | --- | --- | --- | --- |
| `%tid` | 仅输出 TraceId | `traceId` | `TID: 9f8b1c7d2e3a4b5c8d6e7f0a1b2c3d4e.123.456789` | 只想快速定位一条链路时使用，日志更简洁 |
| `%sw_ctx` | 输出完整 SkyWalking 上下文 | `serviceName`、`instanceName`、`traceId`、`traceSegmentId`、`spanId` | `SW_CTX: [demo, 192.168.1.10@8080, traceId, segmentId, 3]` | 需要同时查看链路、实例和 span 信息时使用 |

##### 使用建议

- 想快速 grep 或搜索 TraceId 时，优先看 `%tid`
- 想排查当前日志属于哪个实例、哪个 span 时，优先看 `%sw_ctx`
- 当前项目已经把两者同时打印，控制台中可直接对比效果


##### 日志输出

```shell

       _
      | |
    __| |   ___   _ __ ___     ___
   / _` |  / _ \ | '_ ` _ \   / _ \
  | (_| | |  __/ | | | | | | | (_) |
   \__,_|  \___| |_| |_| |_|  \___/


Application Name: demo
Server Port: 80
Spring Boot Version: 2.4.0 (v2.4.0)
Knife4j 访问地址: http://127.0.0.1:80/doc.html

2026-04-15 14:38:40.750 INFO  [main] [tid=TID:N/A] [ctx=SW_CTX:[demo-skywalking,731161740e1b41e4a883b11342640240@198.18.0.1,N/A,N/A,-1]] com.zhengqing.demo.DemoApplication - Starting DemoApplication using Java 1.8.0_481 on zhengqingyadeMacBook-Air.local with PID 40998 (/Users/zhengqingya/zhengqingya/code/workspace-me/java-workspace/SpringBoot系列/74-SkyWalking全链路/demo-skywalking/target/classes started by zhengqingya in /Users/zhengqingya/zhengqingya/code/workspace-me/java-workspace/SpringBoot系列/74-SkyWalking全链路/demo-skywalking)
2026-04-15 14:38:40.787 DEBUG [main] [tid=TID:N/A] [ctx=SW_CTX:[demo-skywalking,731161740e1b41e4a883b11342640240@198.18.0.1,N/A,N/A,-1]] com.zhengqing.demo.DemoApplication - Running with Spring Boot v2.4.0, Spring v5.3.1
2026-04-15 14:38:40.787 INFO  [main] [tid=TID:N/A] [ctx=SW_CTX:[demo-skywalking,731161740e1b41e4a883b11342640240@198.18.0.1,N/A,N/A,-1]] com.zhengqing.demo.DemoApplication - The following profiles are active: knife4j
2026-04-15 14:38:42.476 INFO  [main] [tid=TID:N/A] [ctx=SW_CTX:[demo-skywalking,731161740e1b41e4a883b11342640240@198.18.0.1,N/A,N/A,-1]] com.zhengqing.demo.DemoApplication - Started DemoApplication in 2.476 seconds (JVM running for 5.943)

2026-04-15 14:38:56.101 INFO  [http-nio-80-exec-1] [tid=TID:13d0ffbd4f7943ad98ce835e77845627.56.17762351360240001] [ctx=SW_CTX:[demo-skywalking,731161740e1b41e4a883b11342640240@198.18.0.1,13d0ffbd4f7943ad98ce835e77845627.56.17762351360240001,13d0ffbd4f7943ad98ce835e77845627.56.17762351360230000,0]] c.zhengqing.demo.api.TestController - time: 2026-04-15 14:38:56
2026-04-15 14:38:57.841 INFO  [http-nio-80-exec-2] [tid=TID:13d0ffbd4f7943ad98ce835e77845627.57.17762351378410001] [ctx=SW_CTX:[demo-skywalking,731161740e1b41e4a883b11342640240@198.18.0.1,13d0ffbd4f7943ad98ce835e77845627.57.17762351378410001,13d0ffbd4f7943ad98ce835e77845627.57.17762351378410000,0]] c.zhengqing.demo.api.TestController - time: 2026-04-15 14:38:57

2026-04-15 14:38:59.236 DEBUG [http-nio-80-exec-3] [tid=TID:13d0ffbd4f7943ad98ce835e77845627.58.17762351392250001] [ctx=SW_CTX:[demo-skywalking,731161740e1b41e4a883b11342640240@198.18.0.1,13d0ffbd4f7943ad98ce835e77845627.58.17762351392250001,13d0ffbd4f7943ad98ce835e77845627.58.17762351392250000,0]] c.zhengqing.demo.api.TestController - SkyWalking log test start, keyword=666
2026-04-15 14:38:59.296 INFO  [http-nio-80-exec-3] [tid=TID:13d0ffbd4f7943ad98ce835e77845627.58.17762351392250001] [ctx=SW_CTX:[demo-skywalking,731161740e1b41e4a883b11342640240@198.18.0.1,13d0ffbd4f7943ad98ce835e77845627.58.17762351392250001,13d0ffbd4f7943ad98ce835e77845627.58.17762351392250000,0]] c.zhengqing.demo.api.TestController - SkyWalking info log, keyword=666, now=2026-04-15 14:38:59
2026-04-15 14:38:59.296 WARN  [http-nio-80-exec-3] [tid=TID:13d0ffbd4f7943ad98ce835e77845627.58.17762351392250001] [ctx=SW_CTX:[demo-skywalking,731161740e1b41e4a883b11342640240@198.18.0.1,13d0ffbd4f7943ad98ce835e77845627.58.17762351392250001,13d0ffbd4f7943ad98ce835e77845627.58.17762351392250000,0]] c.zhengqing.demo.api.TestController - SkyWalking warn log, keyword=666
2026-04-15 14:38:59.298 ERROR [http-nio-80-exec-3] [tid=TID:13d0ffbd4f7943ad98ce835e77845627.58.17762351392250001] [ctx=SW_CTX:[demo-skywalking,731161740e1b41e4a883b11342640240@198.18.0.1,13d0ffbd4f7943ad98ce835e77845627.58.17762351392250001,13d0ffbd4f7943ad98ce835e77845627.58.17762351392250000,0]] c.zhengqing.demo.api.TestController - SkyWalking error log, keyword=666
java.lang.ArithmeticException: / by zero
	at com.zhengqing.demo.api.TestController.$sw$original$logTest$si3kv21(TestController.java:43)
	at com.zhengqing.demo.api.TestController.$sw$original$logTest$si3kv21$accessor$$sw$jm2th61(TestController.java)
	at com.zhengqing.demo.api.TestController$$sw$auxiliary$f417qo0.call(Unknown Source)
	at org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.InstMethodsInter.intercept(InstMethodsInter.java:95)
	at com.zhengqing.demo.api.TestController.logTest(TestController.java)

```
