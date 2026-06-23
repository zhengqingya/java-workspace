#!/usr/bin/env bash

set -euo pipefail # 开启严格模式：命令失败立即退出，未定义变量报错，管道中任一命令失败则整体失败

IMAGE_NAME="registry.cn-hangzhou.aliyuncs.com/zhengqingya/test:demo-k8s-otel-go" # 镜像名称

cd "$(dirname "$0")" # 切换到脚本所在目录，保证相对路径稳定

echo "1、构建 Docker 镜像..."
docker build -f docker/Dockerfile -t "${IMAGE_NAME}" . --no-cache # 使用 docker/Dockerfile 构建镜像，不使用缓存

echo "2、推送 Docker 镜像..."
docker push "${IMAGE_NAME}" # 推送镜像到远程仓库

echo "3、构建并推送完成: ${IMAGE_NAME}"
