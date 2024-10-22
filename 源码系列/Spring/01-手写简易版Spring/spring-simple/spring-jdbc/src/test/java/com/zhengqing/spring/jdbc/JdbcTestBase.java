package com.zhengqing.spring.jdbc;

import cn.hutool.core.io.FileUtil;
import com.zhengqing.spring.io.PropertyResolver;
import org.junit.jupiter.api.BeforeEach;

import java.util.Properties;

public class JdbcTestBase {

    public static final String CREATE_USER = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255) NOT NULL, age INTEGER)";
    public static final String CREATE_ADDRESS = "CREATE TABLE addresses (id INTEGER PRIMARY KEY AUTOINCREMENT, userId INTEGER NOT NULL, address VARCHAR(255) NOT NULL, zip INTEGER)";

    public static final String INSERT_USER = "INSERT INTO users (name, age) VALUES (?, ?)";
    public static final String INSERT_ADDRESS = "INSERT INTO addresses (userId, address, zip) VALUES (?, ?, ?)";

    public static final String UPDATE_USER = "UPDATE users SET name = ?, age = ? WHERE id = ?";
    public static final String UPDATE_ADDRESS = "UPDATE addresses SET address = ?, zip = ? WHERE id = ?";

    public static final String DELETE_USER = "DELETE FROM users WHERE id = ?";
    public static final String DELETE_ADDRESS_BY_USERID = "DELETE FROM addresses WHERE userId = ?";

    public static final String SELECT_USER = "SELECT * FROM users WHERE id = ?";
    public static final String SELECT_USER_NAME = "SELECT name FROM users WHERE id = ?";
    public static final String SELECT_USER_AGE = "SELECT age FROM users WHERE id = ?";
    public static final String SELECT_ADDRESS_BY_USERID = "SELECT * FROM addresses WHERE userId = ?";

    @BeforeEach
    public void beforeEach() {
        cleanDb();
    }

    public PropertyResolver createPropertyResolver() {
        Properties ps = new Properties();
        ps.put("spring.datasource.url", "jdbc:sqlite:/tmp/test.db");
        ps.put("spring.datasource.username", "root");
        ps.put("spring.datasource.password", "123456");
        ps.put("spring.datasource.driver-class-name", "org.sqlite.JDBC");
        PropertyResolver pr = new PropertyResolver(ps);
        return pr;
    }

    void cleanDb() {
        FileUtil.del("/tmp/test.db");
    }
}
