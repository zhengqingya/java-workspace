# Mybatis-Flex

- https://mybatis-flex.com

一个优雅的 Mybatis 增强框架
更轻量、更灵活、以及更高的性能。

### 准备

```
-- demo库中建表
CREATE TABLE IF NOT EXISTS `tb_account`
(
    `id`        INTEGER PRIMARY KEY auto_increment,
    `user_name` VARCHAR(100),
    `age`       Integer,
    `birthday`  DATETIME
);
```

### 请求测试

http://127.0.0.1/sql

com.zhengqing.demo.api.TestController.sql

```
QueryWrapper queryWrapper = QueryWrapper.create()
            .select(
                    ACCOUNT.ID,
                    ACCOUNT.USER_NAME
            )
            .from(ACCOUNT)
            .where(ACCOUNT.ID.eq(1))
            .limit(1);
this.accountMapper.selectListByQuery(queryWrapper);
```

