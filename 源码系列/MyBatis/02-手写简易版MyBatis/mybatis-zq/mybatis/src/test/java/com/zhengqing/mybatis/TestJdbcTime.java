package com.zhengqing.mybatis;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * <p> 原生JDBC </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/20 18:55
 */
public class TestJdbcTime {

    @Test
    public void test() throws Exception {
        // 1、加载JDBC驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 2、建立db连接
        long connectionStart = System.currentTimeMillis();
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mybatis-zq?useUnicode=true&characterEncoding=UTF8&useSSL=false", "root", "root");
        System.out.println("连接耗时：" + (System.currentTimeMillis() - connectionStart));

        long sqlStart = System.currentTimeMillis();

        // 3、构建sql & 执行 -- 查询
        // #{xx} --- ?
        PreparedStatement ps = connection.prepareStatement("select * from t_user where id = ? and name = ?");
        ps.setInt(1, 1);
        ps.setString(2, "zq");
        ps.execute();

        // 4、拿到结果集
        ResultSet rs = ps.getResultSet();
        while (rs.next()) {
            System.out.println(rs.getString("name") + " -- " + rs.getInt("age"));
        }
        System.out.println("sql执行耗时：" + (System.currentTimeMillis() - sqlStart));

        // 5、释放资源
        rs.close();
        ps.close();
        connection.close();
    }

}
