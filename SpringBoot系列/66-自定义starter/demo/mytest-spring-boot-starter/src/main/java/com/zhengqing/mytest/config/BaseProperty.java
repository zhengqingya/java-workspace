package com.zhengqing.mytest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p> 基础配置参数 </p>
 *
 * @author zhengqing
 * @description
 * @date 2020/8/15 16:01
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "base")
public class BaseProperty {

    /**
     * 名称
     */
    private String name;
    /**
     * ip地址
     */
    private String ip;

}
