package com.zhengqing.demo.config.satoken;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


/**
 * <p> Sa-Token 拦截/开放 URL 配置类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/11/3 8:54 下午
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sa-token", ignoreUnknownFields = true)
public class SaTokenUrlConfig {

    /**
     * 拦截url
     */
    private List<String> interceptUrlList;

    /**
     * 开放url
     */
    private List<String> openUrlList;

}
