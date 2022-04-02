package com.zhengqing.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

/**
 * <p> 资源服务器配置 </p>
 *
 * @author zhengqingya
 * @description 授权服务器配置 {@link com.zhengqing.auth.config.AuthorizationServerConfig }
 * @date 2022/4/1 14:27
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /**
     * 配置一个 RemoteTokenServices 的实例，因为资源服务器和授权服务器是分开的；
     * 如果资源服务器和授权服务器是放在一起的，就不需要配置 RemoteTokenServices 了。
     * 校验客户端见 {@link com.zhengqing.auth.config.AuthorizationServerConfig#configure(org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer)}
     * <p>
     * 当用户来资源服务器请求资源时，会携带上一个 access_token，通过这里的配置，就能够校验出 token 是否正确等。
     */
    @Bean
    RemoteTokenServices tokenServices() {
        RemoteTokenServices services = new RemoteTokenServices();
        // access_token 的校验地址
        services.setCheckTokenEndpointUrl("http://127.0.0.1:10010/oauth/check_token");
        services.setClientId("zq_app_id");
        services.setClientSecret("zq_app_secret");
        return services;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("res1").tokenServices(this.tokenServices());
    }

    /**
     * 配置资源的拦截规则
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("admin")
                .anyRequest().authenticated()
                // 支持跨域
                .and()
                .cors();
    }
}
