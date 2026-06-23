# demo-k8s-otel-java

### 一、本地运行

正常启动 Spring Boot 服务即可。

```bash
mvn spring-boot:run
```

本地直接启动时没有 K8s 自动注入，只能验证接口结构和日志格式。

### 二、OTel 接入配置

Java 服务通过 K8s OpenTelemetry Operator 自动注入接入，不需要在应用启动参数中手动添加 Java Agent。

K8s 自动注入时会挂载 OpenTelemetry Java Agent，并通过环境变量指定服务名、OTLP 上报地址和日志 MDC 开关。

关键配置：

```yaml
OTEL_SERVICE_NAME=demo-k8s-otel-java
OTEL_EXPORTER_OTLP_ENDPOINT=http://otel-collector.skywalking.svc.cluster.local:4317
OTEL_EXPORTER_OTLP_PROTOCOL=grpc
OTEL_INSTRUMENTATION_LOGBACK_MDC_ENABLED=true
```

### 三、日志配置

#### 1、Java 项目中配置日志输出格式

配置位置：[`src/main/resources/application.yml`](src/main/resources/application.yml)

```yaml
logging:
  pattern:
    level: "%5p [trace_id=%X{trace_id} span_id=%X{span_id} trace_flags=%X{trace_flags}]"
```

#### 2、K8s 自动注入时开启 Logback MDC

配置位置：`instrumentation-java.yaml`

```yaml
- name: OTEL_INSTRUMENTATION_LOGBACK_MDC_ENABLED
  value: "true"
```

请求接口后，日志中会出现类似：

```text
[trace_id=xxx span_id=xxx trace_flags=xx]
```

### 四、请求接口

```bash
curl -s "http://127.0.0.1/hello?name=java"
curl -s "http://127.0.0.1/chain?targetName=python"
curl -s "http://127.0.0.1/chain?targetName=python&targetUrl=http://127.0.0.1:18081/hello?name=from-java"
curl -s "http://127.0.0.1/trace/final?name=from-php"
```
