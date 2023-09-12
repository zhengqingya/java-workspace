### 一、前言

本文基于`springboot 2.4.0`整合`MongoDB`，实现简单的`增删改查`。

### 二、docker-compose安装MongoDB和可视化图形工具adminMongo

> 温馨小提示：具体配置查看 [https://gitee.com/zhengqingya/docker-compose](https://gitee.com/zhengqingya/docker-compose)

```shell
git clone https://gitee.com/zhengqingya/docker-compose.git
cd docker-compose/Liunx
# 运行
docker-compose -f docker-compose-mongodb.yml -p mongodb up -d
```

访问可视化图形界面：`http://ip地址:1234`
添加连接，Connection string：`mongodb://admin:123456@ip地址:27017`
![](./images/20230912141306975.png)
创建`test-db`
![](./images/20230912141307038.png)
刷新浏览器查看
![](./images/20230912141307077.png)

### 三、SpringBoot整合MongoDB

#### 1、`pom.xml`中引入依赖

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

#### 2、`application.yml`中配置

```yml
# MongoDB 配置
spring:
  data:
    mongodb:
      uri: mongodb://admin:123456@192.168.0.88:27017/test-db?authSource=admin # TODO 根据自己的配置修改
```

#### 3、测试CRUD

```java
@Slf4j
@RestController
@RequestMapping("")
public class TestController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostMapping("add")
    public void add() {
        log.info("add");
        mongoTemplate.save(User.builder().id(1L).username("admin").nickname("管理员").build());
        mongoTemplate.save(User.builder().id(2L).username("test").nickname("测试员").build());
    }

    @DeleteMapping("delete")
    public void delete() {
        log.info("delete");
        mongoTemplate.remove(User.builder().id(2L).build());
    }

    @PutMapping("update")
    public void update() {
        log.info("update");
        Query query = new Query(Criteria.where("id").is(1L));
        Update update = new Update();
        update.set("username", "admin01");
        update.set("nickname", "管理员01");
        mongoTemplate.upsert(query, update, User.class);
    }

    @GetMapping("select")
    public void select() {
        log.info("select");
        Query query = new Query(Criteria.where("id").is(1L));
        User user = mongoTemplate.findOne(query, User.class);
        log.info(JSONObject.toJSONString(user));
    }


}

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
class User implements Serializable {

    @Id
    private Long id;

    private String username;

    private String nickname;

}
```

api： [http://127.0.0.1/doc.html#/home](http://127.0.0.1/doc.html#/home)
![](./images/20230912141307129.png)

查看adminMongo
![](./images/20230912141307170.png)

### 本文案例demo源码

[https://gitee.com/zhengqingya/java-workspace](https://gitee.com/zhengqingya/java-workspace)

---

> 今日分享语句：
> 必须记住我们学习的时间是有限的。时间有限，不只由于人生短促，更由于人事纷繁。我们就应力求把我们所有的时间用去做最有益的事情。


