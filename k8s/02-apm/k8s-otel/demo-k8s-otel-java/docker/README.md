### 生成 jar

```shell
mvn clean -Dmaven.test.skip=true package
cd docker
```

### 使用示例命令

此版本提供出一个`JAVA_OPTS`去设置jar的运行参数

```shell
# 打包镜像 -f:指定Dockerfile文件路径 --no-cache:构建镜像时不使用缓存
docker build -f Dockerfile --build-arg JAVA_OPTS="-XX:+UseG1GC" -t "registry.cn-hangzhou.aliyuncs.com/zhengqingya/test:demo-k8s-otel-java" . --no-cache

# 推送镜像
docker push registry.cn-hangzhou.aliyuncs.com/zhengqingya/test:demo-k8s-otel-java

# 拉取镜像
docker pull registry.cn-hangzhou.aliyuncs.com/zhengqingya/test:demo-k8s-otel-java

# 运行
docker run -d -p 8080:666 --name demo-k8s-otel-java -e server.port=666 registry.cn-hangzhou.aliyuncs.com/zhengqingya/test:demo-k8s-otel-java

# 删除旧容器
docker ps -aq -f name=^/demo-k8s-otel-java$ | xargs -r docker rm -f

# 删除旧镜像
docker images -q -f reference="registry.cn-hangzhou.aliyuncs.com/zhengqingya/test:demo-k8s-otel-java" | xargs -r docker rmi -f 
# docker images | grep -E test | grep demo-k8s-otel-java | awk '{print $3}' | uniq | xargs -I {} docker rmi --force {}
```
