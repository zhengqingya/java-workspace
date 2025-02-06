package com.zhengqing.mybatis.demo.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import cn.hutool.core.date.DateTime;
import com.alibaba.druid.pool.DruidDataSource;
import com.zhengqing.mybatis.demo.entity.User;
import com.zhengqing.mybatis.demo.service.UserService;
import com.zhengqing.mybatis.session.SqlSession;
import com.zhengqing.mybatis.session.SqlSessionFactory;
import com.zhengqing.mybatis.session.SqlSessionFactoryBuilder;
import com.zhengqing.mybatis.spring.annotation.MapperScan;
import com.zhengqing.mybatis.spring.transaction.SpringManagedTransaction;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
@ComponentScan("com.zhengqing.mybatis.demo")
@MapperScan("com.zhengqing.mybatis.demo.mapper")
public class MyBatisConfig {

    @Bean
    public SqlSession sqlSession(DataSource dataSource) {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(dataSource, new SpringManagedTransaction(dataSource), "com.zhengqing.mybatis.demo.mapper");
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


    public static void main(String[] args) {
        // Logback运行时动态更改日志级别
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("ROOT").setLevel(Level.INFO);

        // spring应用上下文 -- 加载配置类，执行自动扫描
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MyBatisConfig.class);
        UserService userService = applicationContext.getBean(UserService.class);
//        System.out.println(JSONUtil.toJsonStr(userService.findOne(1)));
        userService.save(User.builder().name(DateTime.now() + "zq").age(18).build());
    }

}
