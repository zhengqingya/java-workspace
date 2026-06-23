# demo-k8s-agent-python

### 一、本地运行

使用 conda 环境启动：

```bash
# conda create -n python3.11 python=3.11 -y
conda activate python3.11
pip install -r requirements.txt
./run.sh
```

### 二、Agent 接入配置

启动时通过 `sw-python run` 接入 SkyWalking Python Agent：

```bash
SW_AGENT_NAME=demo-k8s-agent-python
SW_AGENT_PROTOCOL=grpc
SW_AGENT_COLLECTOR_BACKEND_SERVICES=127.0.0.1:11800
sw-python run python app.py
```

### 三、日志配置

启动脚本已开启 Python Agent 日志上报：

```bash
SW_AGENT_LOG_REPORTER_ACTIVE=true
SW_AGENT_LOG_REPORTER_LEVEL=INFO
SW_AGENT_LOG_REPORTER_FORMATTED=true
SW_AGENT_LOG_REPORTER_LAYOUT="%(asctime)s %(levelname)s [%(name)s] %(message)s"
```

控制台日志会输出当前 SkyWalking 上下文：

```text
trace_id=<traceId> segment_id=<segmentId> span_id=<spanId>
```

### 四、请求接口

```bash
curl -s "http://127.0.0.1:18081/hello?name=python"
curl -s "http://127.0.0.1:18081/chain?targetName=go"
curl -s "http://127.0.0.1:18081/chain?targetName=go&targetUrl=http://127.0.0.1:18082/hello?name=from-python"
```
