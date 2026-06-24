# syntax=docker/dockerfile:1.7

# 使用 Python 运行环境
FROM python:3.11-slim

# 配置 Python 和 pip，加速国内环境依赖下载，并减少无用版本检查输出
ENV PIP_INDEX_URL=https://mirrors.aliyun.com/pypi/simple/ \
    PIP_TRUSTED_HOST=mirrors.aliyun.com \
    PIP_DISABLE_PIP_VERSION_CHECK=1 \
    PYTHONDONTWRITEBYTECODE=1 \
    PYTHONUNBUFFERED=1

# 设置应用工作目录
WORKDIR /app

# 安装项目依赖
COPY requirements.txt .
RUN --mount=type=cache,target=/root/.cache/pip \
    pip install -r requirements.txt

# 复制应用代码
COPY app.py .

# 对外暴露服务端口
EXPOSE 18081

# 启动 FastAPI 服务
CMD ["python", "app.py"]
