package com.zhengqing.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * <p> Security 核心配置类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/4/1 11:50
 */
@Order(1)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置用户 -- 校验用户
     * 校验客户端见 {@link com.zhengqing.auth.config.AuthorizationServerConfig#configure(org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer)}
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                // admin
                .withUser("admin")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .roles("admin")
                // test
                .and()
                .withUser("test")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .roles("test");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 权限配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 禁用CSRF 表单登录
        http.csrf().disable().formLogin();
    }

}
