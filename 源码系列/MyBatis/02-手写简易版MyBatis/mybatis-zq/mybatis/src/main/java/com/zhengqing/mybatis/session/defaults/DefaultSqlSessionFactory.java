package com.zhengqing.mybatis.session.defaults;

import com.zhengqing.mybatis.session.Configuration;
import com.zhengqing.mybatis.session.SqlSession;
import com.zhengqing.mybatis.session.SqlSessionFactory;
import com.zhengqing.mybatis.transaction.Transaction;

/**
 * <p> 默认的SqlSessionFactory </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/23 01:58
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return this.openSession(true);
    }

    @Override
    public SqlSession openSession(boolean autoCommit) {
        Transaction transaction = this.configuration.getTransaction(autoCommit);
        return new DefaultSqlSession(this.configuration, this.configuration.newExecutor(transaction));
    }
}
