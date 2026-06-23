# 使用 PHP CLI 运行环境
FROM php:8.3-cli-alpine

# 设置应用工作目录
WORKDIR /app

# 复制应用代码
COPY public ./public
COPY src ./src

# 对外暴露服务端口
EXPOSE 18083

# 启动 PHP 内置 HTTP 服务
CMD ["php", "-S", "0.0.0.0:18083", "-t", "public", "public/index.php"]
