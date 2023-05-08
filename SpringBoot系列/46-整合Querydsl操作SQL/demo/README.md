# Querydsl

- https://github.com/querydsl/querydsl
- http://querydsl.com

可以为多个后端构建类型安全的类 SQL 查询，包括 JPA、MongoDB 和 Java 中的 SQL。
它们不是将查询编写为内联字符串或将它们外部化到 XML 文件中，而是通过流畅的 API 构建。

### 准备

1. 建库`querydsl`
2. 编译项目 `mvn clean compile`  =》 生成 QUser 实体类...

### 请求测试

http://127.0.0.1/sql

com.zhengqing.demo.api.TestController.sql

```
this.jpaQueryFactory
                .selectFrom(QUser.user)
                .where(QUser.user.id.eq(1))
                .fetchOne();
```

或

```
this.userRepositoryDls.findOne(QUser.user.id.eq(1));
```

