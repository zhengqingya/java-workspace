# demo-k8s-otel-go

### 一、本地运行

```bash
go run .
```

或：

```bash
./run.sh
```

### 二、OTel 接入配置

Go 服务通过 OTel SDK 接入链路追踪、指标和日志关联，不依赖运行时自动注入。

配置位置：[`main.go`](main.go)

#### 1、Trace

```go
otelhttp.NewHandler(...)
otelhttp.NewTransport(...)
```

服务端请求和客户端请求都会创建/传播 OTel Trace。

#### 2、Metrics

项目通过 OTel SDK 初始化 `MeterProvider`，并通过 OTLP gRPC 上报 HTTP Server/Client 指标：

```go
otlpmetricgrpc.New(...)
sdkmetric.NewPeriodicReader(...)
```

这部分用于让 Go 服务更接近 Java/Python 自动注入后的指标上报效果。

#### 3、K8s 中指定 OTel Collector 地址

配置位置：`demo-k8s-otel-go.yaml`

```yaml
OTEL_SERVICE_NAME=demo-k8s-otel-go
OTEL_EXPORTER_OTLP_ENDPOINT=http://otel-collector.skywalking.svc.cluster.local:4317
OTEL_EXPORTER_OTLP_PROTOCOL=grpc
OTEL_METRIC_EXPORT_INTERVAL=10000
```

Go 服务通过 SDK 直接上报 OTLP gRPC Trace、Metrics 和 Logs 到 Collector。

### 三、日志配置

项目使用 `slog.Handler` 包装器，从 `context.Context` 中读取当前 Span，并自动追加链路字段：

```text
trace_id
span_id
trace_flags
```

### 四、请求接口

```bash
curl -s "http://127.0.0.1:18082/hello?name=go"
curl -s "http://127.0.0.1:18082/chain?targetName=python"
curl -s "http://127.0.0.1:18082/chain?targetName=python&targetUrl=http://127.0.0.1:18081/hello?name=from-go"
```

### 五、eBPF sidecar vs 当前 Go SDK 方式

| 方案 | 优点 | 缺点 | 适用场景 |
| --- | --- | --- | --- |
| eBPF sidecar 自动注入 | 对业务代码侵入少，适合平台侧统一尝试零侵入接入 | 依赖内核、容器权限和 K8s 安全策略；排查成本更高；日志中自动带 `trace_id` 不如 SDK 稳定 | PoC、零侵入探索、统一采集基础 HTTP/gRPC 流量 |
| 当前 Go SDK 方式 | 稳定、主流、可控；HTTP Server/Client 链路清晰；日志可通过 `slog.Handler` 自动追加 `trace_id`、`span_id` | 需要改代码并引入 OTel 依赖；每个 Go 服务都要按规范接入 | 生产推荐方式，需要稳定链路、日志关联和可维护性 |

本项目采用当前 Go SDK 方式，方便稳定验证链路追踪和日志链路信息。
