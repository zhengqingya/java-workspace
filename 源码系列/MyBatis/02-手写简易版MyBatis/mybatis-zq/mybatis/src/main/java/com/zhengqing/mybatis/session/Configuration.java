package com.zhengqing.mybatis.session;

import com.google.common.collect.Maps;
import com.zhengqing.mybatis.datasource.PooledDataSource;
import com.zhengqing.mybatis.executor.CacheExecutor;
import com.zhengqing.mybatis.executor.Executor;
import com.zhengqing.mybatis.executor.SimpleExecutor;
import com.zhengqing.mybatis.executor.parameter.DefaultParameterHandler;
import com.zhengqing.mybatis.executor.parameter.ParameterHandler;
import com.zhengqing.mybatis.executor.resultset.DefaultResultSetHandler;
import com.zhengqing.mybatis.executor.resultset.ResultSetHandler;
import com.zhengqing.mybatis.executor.statement.PreparedStatementHandler;
import com.zhengqing.mybatis.executor.statement.StatementHandler;
import com.zhengqing.mybatis.mapping.MappedStatement;
import com.zhengqing.mybatis.plugin.InterceptorChain;
import com.zhengqing.mybatis.plugin.LimitInterceptor;
import com.zhengqing.mybatis.plugin.SqlLogInterceptor;
import com.zhengqing.mybatis.transaction.JdbcTransaction;
import com.zhengqing.mybatis.transaction.Transaction;
import com.zhengqing.mybatis.type.IntegerTypeHandler;
import com.zhengqing.mybatis.type.StringTypeHandler;
import com.zhengqing.mybatis.type.TypeHandler;
import lombok.Data;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p> 核心配置 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/22 18:11
 */
@Data
public class Configuration {

    private Map<Class, TypeHandler> typeHandlerMap = Maps.newHashMap();

    // eg: com.zhengqing.demo.mapper.UserMapper.selectList --> mapper配置信息
    private Map<String, MappedStatement> mappedStatements = new HashMap<>();

    private InterceptorChain interceptorChain = new InterceptorChain();
    private Transaction transaction;
    private boolean isSpringTransaction = false;
    private DataSource dataSource = new PooledDataSource();
    private boolean cacheEnabled = true;

    public Configuration() {
        this.typeHandlerMap.put(Integer.class, new IntegerTypeHandler());
        this.typeHandlerMap.put(String.class, new StringTypeHandler());

        // 添加插件
        this.interceptorChain.addInterceptor(new LimitInterceptor());
        this.interceptorChain.addInterceptor(new SqlLogInterceptor());
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        this.isSpringTransaction = true;
    }

    public Transaction getTransaction(boolean autoCommit) {
        if (this.isSpringTransaction) {
            return this.transaction;
        }
        return new JdbcTransaction(this.dataSource, autoCommit);
    }

    public void addMappedStatement(MappedStatement ms) {
        this.mappedStatements.put(ms.getId(), ms);
    }

    public MappedStatement getMappedStatement(String id) {
        return this.mappedStatements.get(id);
    }

    public Executor newExecutor(Transaction transaction) {
        Executor executor = new SimpleExecutor(this, transaction);
        if (this.cacheEnabled) {
            executor = new CacheExecutor(executor);
        }
        return (Executor) this.interceptorChain.pluginAll(executor);
    }

    public ResultSetHandler newResultSetHandler() {
        return (ResultSetHandler) this.interceptorChain.pluginAll(new DefaultResultSetHandler(this));
    }

    public ParameterHandler newParameterHandler() {
        return (ParameterHandler) this.interceptorChain.pluginAll(new DefaultParameterHandler(this));
    }

    public StatementHandler newStatementHandler(MappedStatement ms, Object parameter) {
        return (StatementHandler) this.interceptorChain.pluginAll(new PreparedStatementHandler(this, ms, parameter));
    }


}
