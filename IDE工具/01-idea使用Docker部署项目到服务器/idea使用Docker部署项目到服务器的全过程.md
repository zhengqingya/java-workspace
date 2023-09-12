### 一、前言
> **温馨小提示**: 案例源码demo放文章最后了
###### 基本环境
1. idea
2. CentOS7.3服务器
3. docker
4. springboot项目
### 二、服务器配置docker远程连接
> 可参考小编的另外一篇文章：[https://blog.csdn.net/qq_38225558/article/details/100016217](https://blog.csdn.net/qq_38225558/article/details/100016217)

```shell
vi /lib/systemd/system/docker.service  # ① 修改宿主机配置文件
systemctl daemon-reload && systemctl restart docker # ② 重启docker
firewall-cmd --zone=public --add-port=2375/tcp --permanent # ③ 防火墙开放端口2375
```
### 三、idea使用docker部署项目
#### 1. idea安装docker插件
![](./images/20230912143931543.png)
#### 2. 配置远程连接docker
![](./images/20230912143931625.png)
连接成功后我们便可查看docker下的容器和镜像
![](./images/20230912143931708.png)
#### 3. 配置项目 `pom.xml` 文件
```xml
<properties>
    <docker.image.prefix>docker-demo</docker.image.prefix>
</properties>
<build>
    <plugins>
        <!-- maven打包插件 -> 将整个工程打成一个 fatjar -->
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
        <!-- docker构建插件 -->
        <plugin>
            <groupId>com.spotify</groupId>
            <artifactId>docker-maven-plugin</artifactId>
            <version>1.0.0</version>
            <configuration>
            	<!-- 生成的docker镜像名称  -->
                <imageName>${docker.image.prefix}/${project.artifactId}</imageName>
                <dockerDirectory>${project.basedir}/src/main/docker</dockerDirectory>
                <resources>
                    <resource>
                        <targetPath>/</targetPath>
                        <directory>${project.build.directory}</directory>
                        <include>${project.build.finalName}.jar</include>
                    </resource>
                </resources>
            </configuration>
        </plugin>
        <!-- 复制jar包到指定目录 -->
        <plugin>
            <artifactId>maven-antrun-plugin</artifactId>
            <executions>
                <execution>
                    <phase>package</phase>
                    <configuration>
                        <tasks>
                            <copy todir="src/main/docker" file="target/${project.artifactId}-${project.version}.${project.packaging}"></copy>
                        </tasks>
                    </configuration>
                    <goals>
                        <goal>run</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```
#### 4. 在项目 `src/main` 目录下创建 `docker` 目录与 `Dockerfile` 文件
```shell
# 指定基础镜像 这里springboot项目运行只需要java jdk环境即可
FROM java:latest
# 维护者信息
MAINTAINER zq
# 将本地的可执行文件拷贝到Docker容器中的根目录下
COPY app.sh /
#给app.sh赋予可执行权限
RUN chmod +x /app.sh
# 重命名
ADD *.jar app.jar
# 对外暴漏的端口号
EXPOSE 9100
# 运行
ENTRYPOINT ["/app.sh"] # 方式一
#ENTRYPOINT ["java", "-jar", "app.jar"]  # 方式二
```
上面方式一中需要的 `app.sh` 文件内容如下
```shell
#!/bin/bash
java -jar app.jar
```
#### 5. 配置运行
![](./images/20230912143931811.png)
![](./images/20230912143931879.png)
![](./images/20230912143931956.png)
点击运行
![](./images/20230912143932137.png)
构建成功之后如下：
![](./images/20230912143932213.png)
选择 `Log` 即可查看项目输出日志信息
![](./images/20230912143932304.png)
#### 6. 最后浏览器测试访问成功
![](./images/20230912143932403.png)

---

###### 最后奉上源码demo以供参考：[https://download.csdn.net/download/qq_38225558/11595170](https://download.csdn.net/download/qq_38225558/11595170)
整体项目结构如下，相对入门比较简单
![](./images/20230912143932479.png)
