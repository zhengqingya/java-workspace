package com.zhengqing.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.annotation.Resource;


/**
 * <p> 授权服务器配置 </p>
 *
 * @author zhengqingya
 * @description 资源服务器配置 {@link com.zhengqing.user.config.ResourceServerConfig }
 * @date 2022/4/1 11:57
 */
@Configuration
// 开启授权服务器的自动化配置
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Resource
    TokenStore tokenStore;

    @Resource
    ClientDetailsService clientDetailsService;

    /**
     * {@link com.zhengqing.auth.config.SecurityConfig#authenticationManagerBean()}
     */
    @Resource
    AuthenticationManager authenticationManager;

    /**
     * 配置令牌端点的安全约束
     * 即这个端点谁能访问，谁不能访问
     * checkTokenAccess: 指一个 Token 校验的端点，这个端点我们设置为可以直接访问（当资源服务器收到 Token 之后，需要去校验 Token 的合法性，就会访问这个端点）
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

    /**
     * 配置客户端的详细信息 -- 校验客户端
     * 校验用户见 {@link com.zhengqing.auth.config.SecurityConfig#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)}
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                // id
                .withClient("zq_app_id")
                // secret
                .secret(new BCryptPasswordEncoder().encode("zq_app_secret"))
                // 资源id
                .resourceIds("res1")
                // 授权类型 -- 密码模式
                .authorizedGrantTypes("password", "refresh_token")
                // 授权范围
                .scopes("all")
                // 重定向uri
                .redirectUris("http://127.0.0.1:10020/index.html");
    }

    /**
     * 配置令牌的访问端点和令牌服务
     * 授权码和令牌有什么区别？授权码是用来获取令牌的，使用一次就失效，令牌则是用来获取资源的
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                // 配置授权码的存储
//                .authorizationCodeServices(this.authorizationCodeServices())
                //
                .authenticationManager(this.authenticationManager)
                // 配置令牌的存储
                .tokenServices(this.tokenServices());
    }

    /**
     * 配置授权码的存储
     */
    @Bean
    AuthorizationCodeServices authorizationCodeServices() {
        // 内存
        return new InMemoryAuthorizationCodeServices();
    }

    /**
     * 配置 Token 的一些基本信息
     */
    @Bean
    AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setClientDetailsService(this.clientDetailsService);
        // 是否支持刷新
        services.setSupportRefreshToken(true);
        // 存储位置
        services.setTokenStore(this.tokenStore);
        // 有效期
        services.setAccessTokenValiditySeconds(60 * 60 * 1);
        // 刷新token的有效期 -- 当token块过期的时候，需要获取一个新的token，在获取新token的时候，需要一个凭证信息，这个凭证信息不是旧的 Token，而是另外一个 refresh_token，这个 refresh_token 也是有有效期的。
        services.setRefreshTokenValiditySeconds(60 * 60 * 24 * 3);
        return services;
    }

}
