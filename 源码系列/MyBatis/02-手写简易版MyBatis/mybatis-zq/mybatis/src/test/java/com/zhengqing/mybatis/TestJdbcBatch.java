package com.zhengqing.mybatis;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * <p> 原生JDBC </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/20 18:55
 */
public class TestJdbcBatch {

    @Test
    public void test() throws Exception {
        long startTime = System.currentTimeMillis();
        // 1、加载JDBC驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 2、建立db连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mybatis-zq?useUnicode=true&characterEncoding=UTF8&useSSL=false", "root", "root");

        // 3、构建sql & 执行 -- 查询
        // #{xx} --- ?
        PreparedStatement ps = connection.prepareStatement("INSERT INTO `t_user` (`name`, `age`) VALUES (?, ?);");
        for (int i = 0; i < 1000; i++) {
            ps.setString(1, "name: " + i);
            ps.setInt(2, i);
            ps.addBatch();

            // 每100条执行一次批量插入
//            if (i % 100 == 0) {
//                ps.executeBatch();
//                ps.clearBatch();
//            }
        }
        ps.executeBatch(); // 批处理

        // 4、释放资源
        ps.close();
        connection.close();

        System.out.println("耗时：" + (System.currentTimeMillis() - startTime));
        // 单个sql插入数据耗时： 5263 4424  4508
        // batch批量插入数据耗时： 2740 3105 3296

    }

}
