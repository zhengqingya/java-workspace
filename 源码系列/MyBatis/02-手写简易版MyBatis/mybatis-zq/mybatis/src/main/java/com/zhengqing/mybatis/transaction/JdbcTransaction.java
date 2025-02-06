package com.zhengqing.mybatis.transaction;

import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * <p> JDBC事务 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/27 22:00
 */
public class JdbcTransaction implements Transaction {

    private DataSource dataSource;
    private Connection connection;
    private boolean autoCommit;

    public JdbcTransaction(DataSource dataSource, boolean autoCommit) {
        this.dataSource = dataSource;
        this.autoCommit = autoCommit;
    }

    @SneakyThrows
    @Override
    public Connection getConnection() {
        Connection connection = this.dataSource.getConnection();
        connection.setAutoCommit(this.autoCommit);
        this.connection = connection;
        return connection;
    }

    @SneakyThrows
    @Override
    public void commit() {
        if (this.autoCommit) {
            return;
        }
        if (this.connection != null) {
            this.connection.commit();
        }
    }

    @SneakyThrows
    @Override
    public void rollback() {
        if (this.autoCommit) {
            return;
        }
        if (this.connection != null) {
            this.connection.rollback();
        }
    }

    @SneakyThrows
    @Override
    public void close() {
        if (this.connection != null) {
            this.connection.close();
        }
    }
}
