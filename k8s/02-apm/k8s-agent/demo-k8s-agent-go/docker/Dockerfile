# 使用 Go 镜像构建二进制
FROM golang:1.25-alpine AS builder

# SkyWalking Go Agent 版本
ARG SW_GO_AGENT_TGZ_URL=https://downloads.apache.org/skywalking/go/0.6.0/apache-skywalking-go-0.6.0-bin.tgz

# 设置构建工作目录
WORKDIR /src

# 下载 SkyWalking Go Agent 构建期增强程序，独立成缓存层。
RUN set -eux; \
    apk add --no-cache ca-certificates wget tar; \
    mkdir -p /opt/skywalking-go; \
    wget -O /tmp/skywalking-go-agent.tgz "${SW_GO_AGENT_TGZ_URL}"; \
    tar -xzf /tmp/skywalking-go-agent.tgz -C /opt/skywalking-go; \
    agent_path="$(find /opt/skywalking-go -type f \( -name "go-agent" -o -name "*linux-amd64*" \) | head -n 1)"; \
    test -n "${agent_path}"; \
    cp "${agent_path}" /usr/local/bin/go-agent; \
    chmod +x /usr/local/bin/go-agent

# 先复制依赖文件，提升 Docker 构建缓存命中率。
COPY go.mod go.sum ./
RUN go mod download

# 复制源码和 SkyWalking Go Agent 配置
COPY main.go skywalking-go-agent.yaml ./

# 1、通过 go-agent -inject 给当前项目注入 SkyWalking Go 依赖。
# 2、使用 -toolexec 构建已增强的 Linux 可执行文件。
RUN set -eux; \
    mkdir -p /out; \
    /usr/local/bin/go-agent -inject /src; \
    CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -toolexec="/usr/local/bin/go-agent -config /src/skywalking-go-agent.yaml" -a -o /out/demo-k8s-agent-go .

# 使用轻量运行镜像
FROM alpine:3.22

# 设置应用工作目录
WORKDIR /app

# 复制构建产物
COPY --from=builder /out/demo-k8s-agent-go /app/demo-k8s-agent-go

# 对外暴露服务端口
EXPOSE 18082

# 启动 Go 服务
CMD ["/app/demo-k8s-agent-go"]
