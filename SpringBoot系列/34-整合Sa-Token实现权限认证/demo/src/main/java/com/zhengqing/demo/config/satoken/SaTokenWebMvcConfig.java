package com.zhengqing.demo.config.satoken;

import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p> 注册 Sa-Token 路由拦截器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/11/3 12:06
 */
@Configuration
public class SaTokenWebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private SaTokenUrlConfig saTokenUrlConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册1个登录认证拦截器
        registry.addInterceptor(new SaRouteInterceptor())
                .addPathPatterns(this.saTokenUrlConfig.getInterceptUrlList())
                .excludePathPatterns(this.saTokenUrlConfig.getOpenUrlList());
    }

}
