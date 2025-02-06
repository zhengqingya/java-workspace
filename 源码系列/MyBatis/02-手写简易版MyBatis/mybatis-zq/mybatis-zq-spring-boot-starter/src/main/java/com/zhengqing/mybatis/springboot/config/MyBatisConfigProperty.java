package com.zhengqing.mybatis.springboot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p> mybatis属性配置 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/5/5 22:12
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "mybatis-zq")
public class MyBatisConfigProperty {

    /**
     * mapper包扫描路径
     */
    private String mapper;

}
