package com.zhengqing.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * 注释 @EnableTransactionManagement
 * 解决启动失败问题：The bean 'transactionAttributeSource', defined in class path resource [com/github/wujiuye/datasource/tx/TransactionAutoConfig.class], could not be registered. A bean with that name has already been defined in class path resource [org/springframework/transaction/annotation/ProxyTransactionManagementConfiguration.class] and overriding is disabled.
 * 见 https://github.com/wujiuye/easymulti-datasource/issues/28
 */
@EnableTransactionManagement
@SpringBootApplication
// 排除spring boot的数据源自动配置
        (exclude = {
                org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class
        })
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
