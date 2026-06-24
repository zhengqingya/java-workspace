# demo-k8s-otel-php

### 一、本地运行

```bash
# 如果当前 conda 环境没有 PHP，先安装：
conda install -c conda-forge php

./run.sh
```

本地、镜像和 K8s 部署统一使用 PHP CLI 内置 HTTP 服务，方便在 Demo 中保持运行方式一致：

```text
php -S 0.0.0.0:18083 -t public public/index.php
```

### 二、OTel 接入配置

PHP 当前使用轻量手写 OTLP/HTTP JSON 上报器，主动上报服务端 Span、下游客户端 Span 和业务日志，主要用于验证异构链路上下文传播。

项目会从请求头 `traceparent` 中解析 `trace_id`。未携带 `traceparent` 时会生成新的 `trace_id`，并在 `/chain` 请求下游接口时继续透传 `traceparent`。
下游调用失败时会给客户端 Span 和服务端 Span 标记错误状态，方便 Tempo/Grafana 统计错误链路。

K8s 部署配置：

```yaml
Deployment=demo-k8s-otel-php
Service=demo-k8s-otel-php
Port=30085
```

### 三、日志配置

配置位置：[`public/index.php`](public/index.php)

PHP 服务日志会打印当前请求的链路信息：

```text
trace_id=<traceId>
span_id=<spanId>
```

未携带 `traceparent` 请求头时，会生成新的链路信息作为当前 Trace 根节点。

### 四、请求接口

```bash
curl -s "http://127.0.0.1:18083/hello?name=php"
curl -s "http://127.0.0.1:18083/chain?targetName=java"
curl -s "http://127.0.0.1:18083/chain?targetName=java&targetUrl=http://127.0.0.1:18080/hello?name=from-php"
```
