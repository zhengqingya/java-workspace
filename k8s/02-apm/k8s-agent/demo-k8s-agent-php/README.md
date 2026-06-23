# demo-k8s-agent-php

### 一、本地运行

```bash
# 如果当前 conda 环境没有 PHP，先安装：
conda install -c conda-forge php

conda activate python3.11

# 如果本机没有 composer，可用 Docker 安装依赖生成 vendor/autoload.php：
docker run --rm \
  -v "$PWD":/app \
  -w /app \
  composer:2 \
  composer install --no-dev --optimize-autoloader --no-interaction

./run.sh
```

### 二、Agent 接入配置

PHP 通过 `skywalking_agent` 扩展接入 SkyWalking。

本地 `run.sh` 使用 PHP 内置 HTTP 服务，适合验证接口结构；镜像和 K8s 部署使用生产式 `Nginx + PHP-FPM`：

```text
nginx:18083 -> php-fpm:9000 -> public/index.php
```

```bash
SW_AGENT_NAME=demo-k8s-agent-php
SW_AGENT_COLLECTOR_BACKEND_SERVICES=127.0.0.1:11800
skywalking_agent.enable=On
skywalking_agent.reporter_type=grpc
skywalking_agent.service_name=demo-k8s-agent-php
skywalking_agent.server_addr=127.0.0.1:11800
```

### 三、日志配置

PHP 使用 Monolog/PSR-3 输出日志，SkyWalking PHP Agent 通过 `skywalking_agent.psr_logging_level` hook 日志上报：

```text
SW_AGENT_PSR_LOGGING_LEVEL=Info
skywalking_agent.psr_logging_level=Info
```

未加载 `skywalking_agent` 扩展时，`trace_id` 显示 `N/A`。

### 四、请求接口

```bash
curl -s "http://127.0.0.1:18083/hello?name=php"
curl -s "http://127.0.0.1:18083/chain?targetName=java"
curl -s "http://127.0.0.1:18083/chain?targetName=java&targetUrl=http://127.0.0.1:18080/hello?name=from-php"
```
