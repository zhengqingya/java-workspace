# demo-k8s-agent-go

### 一、本地运行

```bash
./run.sh
```

本地直接运行未经过 SkyWalking Go Agent 构建期增强，`trace_id` 会显示 `N/A`。

### 二、Agent 接入配置

Go 通过 SkyWalking Go Agent 构建期增强接入，不是运行时自动注入。

镜像构建阶段会执行：

```bash
go-agent -inject /src
go build -toolexec="/usr/local/bin/go-agent -config /src/skywalking-go-agent.yaml" -a -o /out/demo-k8s-agent-go .
```

关键配置：

```bash
SW_AGENT_NAME=demo-k8s-agent-go
SW_AGENT_REPORTER_GRPC_BACKEND_SERVICE=127.0.0.1:11800
```

### 三、日志配置

服务日志会输出当前 SkyWalking 上下文：

```text
trace_id=<traceId> segment_id=<segmentId> span_id=<spanId>
```

### 四、请求接口

```bash
curl -s "http://127.0.0.1:18082/hello?name=go"
curl -s "http://127.0.0.1:18082/chain?targetName=php"
curl -s "http://127.0.0.1:18082/chain?targetName=php&targetUrl=http://127.0.0.1:18083/hello?name=from-go"
```
