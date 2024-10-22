package com.zhengqing.spring.jdbc;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zhengqing.spring.annotation.Autowired;
import com.zhengqing.spring.annotation.Bean;
import com.zhengqing.spring.annotation.Configuration;
import com.zhengqing.spring.annotation.Value;
import com.zhengqing.spring.jdbc.tx.DataSourceTransactionManager;
import com.zhengqing.spring.jdbc.tx.PlatformTransactionManager;
import com.zhengqing.spring.jdbc.tx.TransactionalBeanPostProcessor;

import javax.sql.DataSource;

@Configuration
public class JdbcConfiguration {

    @Bean(destroyMethod = "close")
    DataSource dataSource(
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password,
            @Value("${spring.datasource.driver-class-name:}") String driver,
            @Value("${spring.datasource.maximum-pool-size:20}") int maximumPoolSize,
            @Value("${spring.datasource.minimum-pool-size:1}") int minimumPoolSize,
            @Value("${spring.datasource.connection-timeout:30000}") int connTimeout
    ) {
        HikariConfig config = new HikariConfig();
        config.setAutoCommit(false);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        if (driver != null) {
            config.setDriverClassName(driver);
        }
        config.setMaximumPoolSize(maximumPoolSize);
        config.setMinimumIdle(minimumPoolSize);
        config.setConnectionTimeout(connTimeout);
        return new HikariDataSource(config);
    }

    @Bean
    JdbcTemplate jdbcTemplate(@Autowired DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    TransactionalBeanPostProcessor transactionalBeanPostProcessor() {
        return new TransactionalBeanPostProcessor();
    }

    @Bean
    PlatformTransactionManager platformTransactionManager(@Autowired DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
