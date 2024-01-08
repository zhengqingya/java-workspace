# Apache Pulsar

https://pulsar.apache.org/

一个分布式发布/订阅消息系统。

### 部署独立的Pulsar集群

> https://pulsar.apache.org/docs/3.1.x/getting-started-docker/

```shell
docker run -it -p 6650:6650 -p 8080:8080 --mount source=pulsardata,target=/pulsar/data --mount source=pulsarconf,target=/pulsar/conf registry.cn-hangzhou.aliyuncs.com/zhengqing/pulsar:3.1.2 bin/pulsar standalone
```

> docker-compose部署pulsar参考 https://gitee.com/zhengqingya/docker-compose
