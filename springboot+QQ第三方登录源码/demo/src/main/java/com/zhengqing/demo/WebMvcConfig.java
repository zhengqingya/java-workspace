package com.zhengqing.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *  <p> 解决跨域 </p>
 *
 * @description:
 * @author: zhengqing
 * @date: 2019/9/2 0002 23:53
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // /**: 表示本应用的所有方法都会去处理跨域请求
        registry.addMapping("/**")
        .allowedOrigins("http://localhost:8080")
        // allowedMethods表示允许通过的请求数
        .allowedMethods("*")
        // allowedHeaders则表示允许的请求头
        .allowedHeaders("*");
    }
}
