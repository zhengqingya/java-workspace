package com.zhengqing.mybatis.datasource;

import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

/**
 * <p> 数据库连接池 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/27 19:13
 */
public class PooledDataSource implements DataSource {

    private final int POOL_SIZE = 10;
    private LinkedBlockingQueue<Connection> pool = new LinkedBlockingQueue<>(this.POOL_SIZE);

    @SneakyThrows
    public PooledDataSource() {
        for (int i = 0; i < this.POOL_SIZE; i++) {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mybatis-zq?useUnicode=true&characterEncoding=UTF8&useSSL=false", "root", "root");
            this.pool.add(new PooledConnection(this, connection).getProxy());
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.pool.poll();
    }

    public void returnConnection(Connection connection) {
        try {
            this.pool.add(connection);
        } catch (Exception e) {

        }
    }


    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
