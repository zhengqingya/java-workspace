# 回顾原生JDBC

#### 1、数据准备

```sql
-- DDL
-- DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`
(
    `id`   int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` varchar(50) DEFAULT NULL COMMENT '名称',
    `age`  int(11) DEFAULT NULL COMMENT '年龄',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- DML
INSERT INTO `t_user` (`id`, `name`, `age`)
VALUES (1, 'zq', 18);
INSERT INTO `t_user` (`id`, `name`, `age`)
VALUES (2, '郑清', 18);
INSERT INTO `t_user` (`id`, `name`, `age`)
VALUES (3, 'test', 19);
```

#### 2、jdbc案例代码

```java
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestJdbc {

    @Test
    public void test() throws Exception {
        // 1、加载JDBC驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 2、建立db连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mybatis-zq?useUnicode=true&characterEncoding=UTF8&useSSL=false", "root", "root");

        // 3、构建sql & 执行 -- 查询
        PreparedStatement ps = connection.prepareStatement("select * from t_user");
        ps.execute();

        // 4、拿到结果集
        ResultSet rs = ps.getResultSet();
        while (rs.next()) {
            System.out.println(rs.getString("name") + " -- " + rs.getInt("age"));
        }

        // 5、释放资源
        rs.close();
        ps.close();
        connection.close();
    }

}
```

问题：

- 频繁连接，释放数据库资源
- SQL语句硬编码，难以维护
- 参数和占位符对应问题
- 结果集解析复杂，硬编码
