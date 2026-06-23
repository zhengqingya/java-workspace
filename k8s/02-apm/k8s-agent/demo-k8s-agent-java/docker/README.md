### 使用示例命令

```shell
# 打包并推送镜像
./build-image.sh

# 拉取镜像
docker pull registry.cn-hangzhou.aliyuncs.com/zhengqingya/test:demo-k8s-agent-java

# 运行
docker run -d -p 18080:80 --name demo-k8s-agent-java registry.cn-hangzhou.aliyuncs.com/zhengqingya/test:demo-k8s-agent-java

# 删除旧容器
docker ps -aq -f name=^/demo-k8s-agent-java$ | xargs -r docker rm -f

# 删除旧镜像
docker images -q -f reference="registry.cn-hangzhou.aliyuncs.com/zhengqingya/test:demo-k8s-agent-java" | xargs -r docker rmi -f
```
