# SkyWalking

- 官网：https://skywalking.apache.org/
- github：https://github.com/apache/skywalking
- 文档：https://skywalking.apache.org/docs/

### OTEL 版

OTel 作为各语言的统一数据采集标准，最后汇聚到 SkyWalking OAP 进行分析展示。

OpenTelemetry (简称 OTel) 是云原生时代统一的可观测性全球开源标准，它通过一套跨语言的 API、SDK 和工具，标准化地采集并传输微服务的链路追踪 (Traces)、指标 (Metrics) 和日志 (Logs) 数据，彻底解决了监控体系被特定厂商绑定的问题。

- https://opentelemetry.io/docs/zero-code/java/agent/getting-started/
- https://github.com/open-telemetry/opentelemetry-java-instrumentation

下载eg：https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.26.1/opentelemetry-javaagent.jar

jvm启动参数

```shell
-javaagent:/data/opentelemetry-javaagent.jar
-Dotel.service.name=demo-skywalking-otel
-Dotel.resource.attributes=deployment.environment=dev,service.namespace=default
-Dotel.traces.exporter=otlp
-Dotel.metrics.exporter=otlp
-Dotel.logs.exporter=otlp
-Dotel.exporter.otlp.protocol=grpc
-Dotel.exporter.otlp.endpoint=http://127.0.0.1:4317
```

---

结论：
- SkyWalking 原生 agent 更适合在 SkyWalking 原生页面看链路
- OTel 也能看链路，但在 SkyWalking 里通常走的是 Zipkin/Lens 这套查询入口。https://skywalking.apache.org/docs/main/v10.4.0/en/setup/backend/otlp-trace/

即分为2步查看数据
- 看链路：http://127.0.0.1:18080/zipkin
- 看日志：SkyWalking 原来的 Log 页面

![](./images/README-1776327358582.png)

