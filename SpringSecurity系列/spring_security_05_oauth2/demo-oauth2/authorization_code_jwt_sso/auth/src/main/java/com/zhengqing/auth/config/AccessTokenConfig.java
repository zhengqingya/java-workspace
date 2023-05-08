package com.zhengqing.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * <p> 配置生成token存储 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/4/1 12:00
 */
@Configuration
public class AccessTokenConfig {

//    @Resource
//    RedisConnectionFactory redisConnectionFactory;

    /**
     * JWT 字符串生成时所需签名
     */
    private final String SIGNING_KEY = "zhengqingya";

    @Bean
    TokenStore tokenStore() {
        // 内存
//        return new InMemoryTokenStore();
        // redis
//        return new RedisTokenStore(this.redisConnectionFactory);
        // jwt -- 无状态登录，服务端不需要保存信息
        return new JwtTokenStore(this.jwtAccessTokenConverter());
    }

    /**
     * 实现将用户信息和 JWT 进行转换（将用户信息转为 jwt 字符串，或者从 jwt 字符串提取出用户信息）
     */
    @Bean
    JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(this.SIGNING_KEY);
        return converter;
    }

}
