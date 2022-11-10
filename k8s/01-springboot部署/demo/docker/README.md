### 使用示例命令

```shell
# 打包镜像 -f:指定Dockerfile文件路径 --no-cache:构建镜像时不使用缓存
docker build -f Dockerfile --build-arg JAVA_OPTS="-XX:+UseG1GC -Xms100m -Xmx100m" --build-arg APP_NAME="test" --build-arg APP_PORT="8080" -t 'registry.cn-hangzhou.aliyuncs.com/zhengqingya/springboot-demo' . --no-cache

# 推送镜像
docker push registry.cn-hangzhou.aliyuncs.com/zhengqingya/springboot-demo

# 拉取镜像
docker pull registry.cn-hangzhou.aliyuncs.com/zhengqingya/springboot-demo

# 运行
docker run -d -p 8080:8080 -p 5001:5001 --name springboot-demo registry.cn-hangzhou.aliyuncs.com/zhengqingya/springboot-demo

# 删除旧容器
docker rm -f springboot-demo

# 删除旧镜像
docker rmi -f registry.cn-hangzhou.aliyuncs.com/zhengqingya/springboot-demo
```
