# 使用 Python 运行环境
FROM python:3.11-slim

# 设置应用工作目录
WORKDIR /app

# 安装项目依赖
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# 复制应用代码
COPY app.py .

# 对外暴露服务端口
EXPOSE 18081

# 启动 FastAPI 服务
CMD ["python", "app.py"]
