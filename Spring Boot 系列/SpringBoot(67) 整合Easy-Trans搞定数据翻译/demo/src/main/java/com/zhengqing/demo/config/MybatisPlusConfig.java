package com.zhengqing.demo.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p> MybatisPlus配置类 </p>
 *
 * @author zhengqing
 * @description
 * @date 2019/10/8 11:36
 */
@EnableTransactionManagement
@Configuration
@MapperScan("com.zhengqing.demo.**.mapper*") // 扫描 Mapper 文件夹 TODO 【注：根据自己的项目结构配置】
public class MybatisPlusConfig {


}
