package com.zhengqing.mybatis.springboot.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.zhengqing.mybatis.session.SqlSession;
import com.zhengqing.mybatis.session.SqlSessionFactory;
import com.zhengqing.mybatis.session.SqlSessionFactoryBuilder;
import com.zhengqing.mybatis.spring.transaction.SpringManagedTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * <p> mybatis配置 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/5/5 04:07
 */
@EnableTransactionManagement // 开启事务
@EnableConfigurationProperties(MyBatisConfigProperty.class)
public class MyBatisAutoConfiguration {

    @Autowired
    private MyBatisConfigProperty myBatisConfigProperty;


    @Bean
    public SqlSession sqlSession(DataSource dataSource) {
        String mapper = this.myBatisConfigProperty.getMapper();
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(dataSource, new SpringManagedTransaction(dataSource), mapper);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession;
    }

    // 创建数据库连接池
    @Bean
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/mybatis-zq?useUnicode=true&characterEncoding=UTF8&useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }

    // 创建 JdbcTemplate 对象
    @Bean
    public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }

    // 创建事务管理器
    @Bean
    public DataSourceTransactionManager getDataSourceTransactionManager(DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }


}
