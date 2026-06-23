# demo-k8s-agent-java

### 一、本地运行

正常启动 Spring Boot 服务即可。

```bash
mvn spring-boot:run
```

本地直接启动未添加 SkyWalking Java Agent 时，只能验证接口结构和普通日志输出。

### 二、Agent 接入配置

启动时添加 SkyWalking Java Agent：

```bash
-javaagent:/path/to/skywalking-agent.jar
-Dskywalking.agent.service_name=demo-k8s-agent-java
-Dskywalking.collector.backend_service=127.0.0.1:11800
```

`/path/to/skywalking-agent.jar` 按本机实际 SkyWalking Java Agent 路径替换。

### 三、日志配置

`pom.xml` 引入 SkyWalking 日志和链路工具包：

```xml
<dependency>
    <groupId>org.apache.skywalking</groupId>
    <artifactId>apm-toolkit-logback-1.x</artifactId>
    <version>${skywalking.version}</version>
</dependency>
<dependency>
    <groupId>org.apache.skywalking</groupId>
    <artifactId>apm-toolkit-trace</artifactId>
    <version>${skywalking.version}</version>
</dependency>
```

`logback-spring.xml` 配置链路占位符：

```xml
<conversionRule conversionWord="tid"
                converterClass="org.apache.skywalking.apm.toolkit.log.logback.v1.x.LogbackPatternConverter"/>
<conversionRule conversionWord="sw_ctx"
                converterClass="org.apache.skywalking.apm.toolkit.log.logback.v1.x.LogbackSkyWalkingContextPatternConverter"/>
```

配置 `GRPCLogClientAppender` 将日志上报 SkyWalking：

```xml
<appender name="SKYWALKING" class="org.apache.skywalking.apm.toolkit.log.logback.v1.x.log.GRPCLogClientAppender">
    <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
        <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level [tid=%tid,ctx=%sw_ctx] %logger{36} - %msg%n%ex</pattern>
    </encoder>
</appender>
```

`root` 同时引用控制台和 SkyWalking appender：

```xml
<root level="DEBUG">
    <appender-ref ref="STDOUT"/>
    <appender-ref ref="SKYWALKING"/>
</root>
```

日志中会输出：

```text
[tid=<traceId>] [ctx=<service,instance,traceId,segmentId,spanId>]
```

### 四、请求接口

```bash
curl -s "http://127.0.0.1/hello?name=java"
curl -s "http://127.0.0.1/chain?targetName=python"
curl -s "http://127.0.0.1/chain?targetName=python&targetUrl=http://127.0.0.1/hello?name=from-java"
```
