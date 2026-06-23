# demo-k8s-otel-php

### 一、本地运行

```bash
# 如果当前 conda 环境没有 PHP，先安装：
conda install -c conda-forge php

./run.sh
```

本地 `run.sh` 使用 PHP 内置 HTTP 服务，适合验证接口结构；镜像和 K8s 部署使用生产式 `Nginx + PHP-FPM`：

```text
nginx:18083 -> php-fpm:9000 -> public/index.php
```

### 二、OTel 接入配置

PHP 当前没有使用完整 OTel PHP 自动注入或 SDK 创建 Span，主要用于验证异构链路上下文传播。

项目会从请求头 `traceparent` 中解析 `trace_id`，并在 `/chain` 请求下游接口时继续透传 `traceparent`。

K8s 部署配置：

```yaml
Deployment=demo-k8s-otel-php
Service=demo-k8s-otel-php
Port=30085
```

### 三、日志配置

配置位置：[`public/index.php`](public/index.php)

PHP 服务日志会打印从 `traceparent` 中解析出的链路信息：

```text
trace_id=<traceId>
```

未携带 `traceparent` 请求头时，`trace_id` 显示 `N/A`。

### 四、请求接口

```bash
curl -s "http://127.0.0.1:18083/hello?name=php"
curl -s "http://127.0.0.1:18083/chain?targetName=java"
curl -s "http://127.0.0.1:18083/chain?targetName=java&targetUrl=http://127.0.0.1:18080/hello?name=from-php"
```
