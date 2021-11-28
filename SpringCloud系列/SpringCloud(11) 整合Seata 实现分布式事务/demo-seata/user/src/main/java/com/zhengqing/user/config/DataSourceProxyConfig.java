package com.zhengqing.user.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * <p> 数据源配置 </p>
 *
 * @author zhengqingya
 * @description Seata 是通过代理数据源实现事务分支的，
 * 所以需要配置 io.seata.rm.datasource.DataSourceProxy 的 Bean，
 * 且是 @Primary默认的数据源，否则事务不会回滚，无法实现分布式事务
 * @date 2021/11/26 16:51
 */
//@Configuration
public class DataSourceProxyConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DruidDataSource druidDataSource() {
        return new DruidDataSource();
    }

    @Bean
    @Primary
    public DataSourceProxy dataSource(DruidDataSource druidDataSource) {
        return new DataSourceProxy(druidDataSource);
    }

}
