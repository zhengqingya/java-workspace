#!/usr/bin/env bash

set -eu # 开启严格模式：命令失败立即退出，未定义变量报错

IMAGE_NAME="registry.cn-hangzhou.aliyuncs.com/zhengqingya/test:demo-k8s-agent-php" # 镜像名称
BUILD_ARGS=""

if [ "${NO_CACHE:-0}" = "1" ]; then
  BUILD_ARGS="--no-cache"
fi

cd "$(dirname "$0")" # 切换到脚本所在目录，保证相对路径稳定

echo "1、构建 Docker 镜像..."
docker build -f docker/Dockerfile -t "${IMAGE_NAME}" ${BUILD_ARGS} . # 默认使用 Docker 缓存；NO_CACHE=1 时强制全量构建

echo "2、推送 Docker 镜像..."
docker push "${IMAGE_NAME}" # 推送镜像到远程仓库

echo "3、构建并推送完成: ${IMAGE_NAME}"
