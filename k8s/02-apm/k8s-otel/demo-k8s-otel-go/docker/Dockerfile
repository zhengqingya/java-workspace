# syntax=docker/dockerfile:1.7

# 使用 Go 镜像构建二进制
FROM golang:1.25-alpine AS builder

# 设置构建工作目录
WORKDIR /src

# 配置国内 Go 代理，加速依赖下载
ENV GOPROXY=https://goproxy.cn,direct

# 先复制依赖清单，复用 Docker 依赖缓存层
COPY go.mod go.sum ./

# 下载 Go 依赖，BuildKit 开启时复用模块缓存
RUN --mount=type=cache,target=/go/pkg/mod \
    go mod download

# 复制源码
COPY . .

# 构建 Linux 可执行文件，固定输出路径方便 K8s Go 自动注入定位，复用 Go 编译缓存
RUN --mount=type=cache,target=/go/pkg/mod \
    --mount=type=cache,target=/root/.cache/go-build \
    CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -o /out/demo-k8s-otel-go .

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
