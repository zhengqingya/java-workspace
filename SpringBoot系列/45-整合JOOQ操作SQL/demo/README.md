# jOOQ

- https://github.com/jOOQ/jOOQ
- http://www.jooq.org
- [Spring Boot和jOOQ整合](https://jooq.diamondfsd.com/learn/section-8-spring-boot-jooq.html)

jOOQ is the best way to write SQL in Java

一个ORM框架，利用其生成的Java代码和流畅的API，可以快速构建有类型约束的安全的SQL语句。

### 建表

```sql
CREATE TABLE `t_user`
(
    `id`       int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` varchar(100) NOT NULL COMMENT '账号',
    `nickname` varchar(100) NOT NULL COMMENT '昵称',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户表';
```

### 代码生成

![](jooq-codegen.png)

### 请求测试

http://127.0.0.1/sql

com.zhengqing.demo.api.TestController.sql

```
this.dslContext.insertInto(T_USER, T_USER.USERNAME, T_USER.NICKNAME)
                .values(RandomUtil.randomString(10), RandomUtil.randomString(10))
                .execute();
```

