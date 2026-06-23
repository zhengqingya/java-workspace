# 使用 Go 镜像构建二进制
FROM golang:1.25-alpine AS builder

# 设置构建工作目录
WORKDIR /src

# 复制源码
COPY . .

# 构建 Linux 可执行文件，固定输出路径方便 K8s Go 自动注入定位
RUN CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -o /out/demo-k8s-otel-go .

# 使用轻量运行镜像
FROM alpine:3.22

# 设置应用工作目录
WORKDIR /app

# 复制构建产物
COPY --from=builder /out/demo-k8s-otel-go /app/demo-k8s-otel-go

# 对外暴露服务端口
EXPOSE 18082

# 启动 Go 服务
CMD ["/app/demo-k8s-otel-go"]
