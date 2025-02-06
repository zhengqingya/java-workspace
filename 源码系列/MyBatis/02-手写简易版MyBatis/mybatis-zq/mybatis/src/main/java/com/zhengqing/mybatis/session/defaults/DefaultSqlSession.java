package com.zhengqing.mybatis.session.defaults;

import com.zhengqing.mybatis.binding.MapperProxyFactory;
import com.zhengqing.mybatis.executor.Executor;
import com.zhengqing.mybatis.mapping.MappedStatement;
import com.zhengqing.mybatis.session.Configuration;
import com.zhengqing.mybatis.session.SqlSession;

import java.util.List;

/**
 * <p> 默认SqlSession </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/23 01:24
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;
    private Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public int insert(String statementId, Object parameter) {
        MappedStatement ms = this.configuration.getMappedStatement(statementId);
        return this.executor.update(ms, parameter);
    }

    @Override
    public int delete(String statementId, Object parameter) {
        MappedStatement ms = this.configuration.getMappedStatement(statementId);
        return this.executor.update(ms, parameter);
    }

    @Override
    public int update(String statementId, Object parameter) {
        MappedStatement ms = this.configuration.getMappedStatement(statementId);
        return this.executor.update(ms, parameter);
    }

    @Override
    public <T> T selectOne(String statementId, Object parameter) {
        List<T> list = this.selectList(statementId, parameter);
        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() > 1) {
            throw new RuntimeException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    @Override
    public <T> List<T> selectList(String statementId, Object parameter) {
        MappedStatement ms = this.configuration.getMappedStatement(statementId);
        return this.executor.query(ms, parameter);
    }

    @Override
    public <T> T getMapper(Class<T> mapper) {
        return MapperProxyFactory.getProxy(mapper, this);
    }

    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }

    @Override
    public void commit() {
        this.executor.commit();
    }

    @Override
    public void rollback() {
        this.executor.rollback();
    }

    @Override
    public void close() {
        this.executor.close();
    }

}
