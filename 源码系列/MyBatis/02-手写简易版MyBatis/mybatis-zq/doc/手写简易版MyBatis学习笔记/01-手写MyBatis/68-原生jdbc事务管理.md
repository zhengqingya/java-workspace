# 原生jdbc事务管理

```java
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class TestJdbcTransaction {

    @Test
    public void test() throws Exception {
        // 1、加载JDBC驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 2、建立db连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mybatis-zq?useUnicode=true&characterEncoding=UTF8&useSSL=false", "root", "root");
        System.out.println("是否自动提交：" + connection.getAutoCommit());
        connection.setAutoCommit(false);// 设置手动提交

        // 3、构建sql & 执行 -- 查询
        // #{xx} --- ?
        PreparedStatement ps = connection.prepareStatement("INSERT INTO `t_user` (`name`, `age`) VALUES ('xxx', 18);");
        ps.execute();

        // 4、拿到更新操作数
        System.out.println(ps.getUpdateCount());

        try {
            this.handleBusiness();
            connection.commit(); // 提交事务
        } catch (Exception e) {
            connection.rollback(); // 回滚事务
            throw new RuntimeException(e);
        }

        // 5、释放资源
        ps.close();
        connection.close();
    }

    private void handleBusiness() {
        int i = 1 / 0;// 假设这里是业务逻辑代码
    }
}
```