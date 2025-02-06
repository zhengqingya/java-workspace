package com.zhengqing.mybatis.executor;

import com.zhengqing.mybatis.cache.Cache;
import com.zhengqing.mybatis.cache.PerpetualCache;
import com.zhengqing.mybatis.executor.statement.StatementHandler;
import com.zhengqing.mybatis.mapping.MappedStatement;
import com.zhengqing.mybatis.session.Configuration;
import com.zhengqing.mybatis.transaction.Transaction;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

/**
 * <p> 简单执行器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/22 23:10
 */
public class SimpleExecutor implements Executor {

    private Configuration configuration;
    private Transaction transaction;
    private Cache localCache;

    public SimpleExecutor(Configuration configuration, Transaction transaction) {
        this.configuration = configuration;
        this.transaction = transaction;
        this.localCache = new PerpetualCache("LocalCache");
    }

    @SneakyThrows
    @Override
    public <T> List<T> query(MappedStatement ms, Object parameter) {
        String cacheKey = ms.getCacheKey(parameter);
        Object list = this.localCache.getObject(cacheKey);
        if (list != null) {
            return (List<T>) list;
        }
        StatementHandler statementHandler = this.configuration.newStatementHandler(ms, parameter);
        Statement statement = this.prepareStatement(statementHandler);
        list = statementHandler.query(statement);
        this.localCache.putObject(cacheKey, list);
        return (List<T>) list;
    }

    @SneakyThrows
    @Override
    public int update(MappedStatement ms, Object parameter) {
        this.localCache.clear();
        StatementHandler statementHandler = this.configuration.newStatementHandler(ms, parameter);
        Statement statement = this.prepareStatement(statementHandler);
        return statementHandler.update(statement);
    }

    @Override
    public void commit() {
        this.transaction.commit();
    }

    @Override
    public void rollback() {
        this.transaction.rollback();
    }

    @Override
    public void close() {
        this.transaction.close();
    }

    private Statement prepareStatement(StatementHandler statementHandler) {
        Connection connection = this.getConnection();
        Statement statement = statementHandler.prepare(connection);
        statementHandler.parameterize(statement);
        return statement;
    }


    @SneakyThrows
    private Connection getConnection() {
        return this.transaction.getConnection();
    }


}
