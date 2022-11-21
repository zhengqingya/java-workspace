### 使用示例命令

```shell
# 打包镜像 -f:指定Dockerfile文件路径 --no-cache:构建镜像时不使用缓存
docker build -f Dockerfile --build-arg JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5001 -XX:+UseG1GC -Xms64m -Xmx64m -Xmn16m -XX:MetaspaceSize=100m -XX:MaxMetaspaceSize=100m -XX:MaxGCPauseMillis=200 -XX:ParallelGCThreads=8 -Ddefault.client.encoding=UTF-8 -Dfile.encoding=UTF-8 -Duser.language=Zh -Duser.region=CN" --build-arg APP_NAME="springboot-demo" --build-arg APP_PORT="80" -t "registry.cn-hangzhou.aliyuncs.com/zhengqingya/springboot-demo" . --no-cache

# 推送镜像
docker push registry.cn-hangzhou.aliyuncs.com/zhengqingya/springboot-demo

# 拉取镜像
docker pull registry.cn-hangzhou.aliyuncs.com/zhengqingya/springboot-demo

# 运行
docker run -d -p 80:80 -p 5001:5001 --name springboot-demo registry.cn-hangzhou.aliyuncs.com/zhengqingya/springboot-demo

# 删除旧容器
docker rm -f springboot-demo

# 删除旧镜像
docker rmi -f registry.cn-hangzhou.aliyuncs.com/zhengqingya/springboot-demo
```
