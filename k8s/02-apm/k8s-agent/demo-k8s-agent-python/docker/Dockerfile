# 使用 Python 运行环境
FROM python:3.11-slim

# 设置应用工作目录
WORKDIR /app

# 安装项目依赖
COPY requirements.txt .
RUN apt-get update \
    && apt-get install -y --no-install-recommends gcc python3-dev build-essential \
    && pip install --no-cache-dir -r requirements.txt \
    && apt-get purge -y --auto-remove gcc python3-dev build-essential \
    && rm -rf /var/lib/apt/lists/*

# 复制应用代码
COPY app.py .

# 对外暴露服务端口
EXPOSE 18081

# 启动 FastAPI 服务，并通过 SkyWalking Python Agent 非侵入接入。
CMD ["sh", "-c", "SW_AGENT_LOG_REPORTER_ACTIVE=${SW_AGENT_LOG_REPORTER_ACTIVE:-true} SW_AGENT_LOG_REPORTER_LEVEL=${SW_AGENT_LOG_REPORTER_LEVEL:-INFO} SW_AGENT_LOG_REPORTER_FORMATTED=${SW_AGENT_LOG_REPORTER_FORMATTED:-true} SW_AGENT_LOG_REPORTER_LAYOUT=\"${SW_AGENT_LOG_REPORTER_LAYOUT:-%(asctime)s %(levelname)s [%(name)s] %(message)s}\" sw-python run python app.py"]
