### 使用示例命令

```shell
# 打包镜像 -f:指定Dockerfile文件路径 --no-cache:构建镜像时不使用缓存
docker build -f docker/Dockerfile -t "registry.cn-hangzhou.aliyuncs.com/zhengqingya/test:demo-k8s-otel-go" . --no-cache

# 推送镜像
docker push registry.cn-hangzhou.aliyuncs.com/zhengqingya/test:demo-k8s-otel-go

# 拉取镜像
docker pull registry.cn-hangzhou.aliyuncs.com/zhengqingya/test:demo-k8s-otel-go

# 运行
docker run -d -p 18082:18082 --name demo-k8s-otel-go registry.cn-hangzhou.aliyuncs.com/zhengqingya/test:demo-k8s-otel-go

# 删除旧容器
docker ps -aq -f name=^/demo-k8s-otel-go$ | xargs -r docker rm -f

# 删除旧镜像
docker images -q -f reference="registry.cn-hangzhou.aliyuncs.com/zhengqingya/test:demo-k8s-otel-go" | xargs -r docker rmi -f
# docker images | grep -E test | grep demo-k8s-otel-go | awk '{print $3}' | uniq | xargs -I {} docker rmi --force {}
```
