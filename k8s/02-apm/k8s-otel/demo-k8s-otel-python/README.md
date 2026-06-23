# demo-k8s-otel-python

### 一、本地运行

```bash
# conda create -n python3.11 python=3.11 -y
conda activate python3.11
pip install -r requirements.txt
python app.py
```

本地直接运行没有 K8s 自动注入，项目会自动补默认日志字段，避免普通启动时报 `otelTraceID` 等字段缺失。

### 二、OTel 接入配置

Python 服务通过 K8s OpenTelemetry Operator 自动注入接入。

K8s 自动注入时会通过 Python instrumentation 启动应用，并通过环境变量指定服务名、OTLP 上报地址和日志关联开关。

关键配置：

```yaml
OTEL_SERVICE_NAME=demo-k8s-otel-python
OTEL_EXPORTER_OTLP_ENDPOINT=http://otel-collector.skywalking.svc.cluster.local:4317
OTEL_EXPORTER_OTLP_PROTOCOL=grpc
OTEL_PYTHON_LOG_CORRELATION=true
```

### 三、日志配置

#### 1、Python 项目中配置日志输出格式

配置位置：[`app.py`](app.py)

日志格式中预留 OTel Python 自动注入的链路字段：

```text
trace_id=%(otelTraceID)s span_id=%(otelSpanID)s sampled=%(otelTraceSampled)s service=%(otelServiceName)s
```

#### 2、K8s 自动注入时开启日志关联

配置位置：`instrumentation-python.yaml`

```yaml
- name: OTEL_PYTHON_LOG_CORRELATION
  value: "true"
```

开启后，请求进入 Python 服务时，日志中会自动带上：

```text
trace_id=<traceId> span_id=<spanId> sampled=<true|false> service=<serviceName>
```

### 四、请求接口

```bash
curl -s "http://127.0.0.1:18081/hello?name=python"
curl -s "http://127.0.0.1:18081/chain?targetName=php"
curl -s "http://127.0.0.1:18081/chain?targetName=php&targetUrl=http://127.0.0.1:18083/chain?name=from-python"
```
