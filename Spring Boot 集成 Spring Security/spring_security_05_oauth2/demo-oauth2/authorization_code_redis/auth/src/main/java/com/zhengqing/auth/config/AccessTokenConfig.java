package com.zhengqing.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;

/**
 * <p> 配置生成token存储 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/4/1 12:00
 */
@Configuration
public class AccessTokenConfig {

    @Resource
    RedisConnectionFactory redisConnectionFactory;

    @Bean
    TokenStore tokenStore() {
        // 内存
//        return new InMemoryTokenStore();
        // redis
        return new RedisTokenStore(this.redisConnectionFactory);
    }

}
