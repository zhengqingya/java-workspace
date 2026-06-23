### 使用示例命令

```shell
# 打包镜像 -f:指定Dockerfile文件路径，默认使用 Docker 缓存
docker build -f docker/Dockerfile -t "registry.cn-hangzhou.aliyuncs.com/zhengqingya/test:demo-k8s-agent-python" .

# 推送镜像
docker push registry.cn-hangzhou.aliyuncs.com/zhengqingya/test:demo-k8s-agent-python

# 拉取镜像
docker pull registry.cn-hangzhou.aliyuncs.com/zhengqingya/test:demo-k8s-agent-python

# 运行
docker run -d -p 18081:18081 --name demo-k8s-agent-python registry.cn-hangzhou.aliyuncs.com/zhengqingya/test:demo-k8s-agent-python

# 删除旧容器
docker ps -aq -f name=^/demo-k8s-agent-python$ | xargs -r docker rm -f

# 删除旧镜像
docker images -q -f reference="registry.cn-hangzhou.aliyuncs.com/zhengqingya/test:demo-k8s-agent-python" | xargs -r docker rmi -f
```
