package com.zhengqing.common.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * <p>
 * 全局配置解决跨域
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2019/8/22 9:09
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfig = new CorsConfiguration();
        // 设置你要允许的网站域名，如果全允许则设为 *
//        corsConfig.addAllowedOrigin("*");
//        corsConfig.addAllowedOrigin("http://www.zhengqingya.com");
        // 当allowCredentials为true时，allowedOrigins不能包含特殊值"*"，因为它不能在"Access-Control-Allow-Origin"响应头中设置。
        // 要允许凭证指向一组起源，可以显式地列出它们，考虑使用“allowedOriginPatterns”代替。
        corsConfig.addAllowedOriginPattern("*");
        // 如果要限制 HEADER 或 METHOD 请自行更改
        corsConfig.addAllowedHeader("*");
        // 设置允许的方法
        corsConfig.addAllowedMethod("*");
        // 是否允许证书
        corsConfig.setAllowCredentials(true);
        // 设置允许跨域请求的路由
        source.registerCorsConfiguration("/**", corsConfig);
        return new CorsWebFilter(source);
    }

}
