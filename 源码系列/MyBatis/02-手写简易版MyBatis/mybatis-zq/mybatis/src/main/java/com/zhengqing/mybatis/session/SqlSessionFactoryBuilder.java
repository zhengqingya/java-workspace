package com.zhengqing.mybatis.session;

import com.zhengqing.mybatis.builder.XMLConfigBuilder;
import com.zhengqing.mybatis.session.defaults.DefaultSqlSessionFactory;
import com.zhengqing.mybatis.transaction.Transaction;

import javax.sql.DataSource;

/**
 * <p> SqlSession工厂构建者 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/23 02:09
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build() {
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parse();
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        return sqlSessionFactory;
    }

    public SqlSessionFactory build(DataSource dataSource, Transaction transaction) {
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parse(dataSource, transaction, null);
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        return sqlSessionFactory;
    }

    public SqlSessionFactory build(DataSource dataSource, Transaction transaction, String mapperPackageName) {
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parse(dataSource, transaction, mapperPackageName);
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        return sqlSessionFactory;
    }


}
