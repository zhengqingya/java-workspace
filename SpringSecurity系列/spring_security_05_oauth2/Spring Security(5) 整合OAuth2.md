@[TOC](文章目录)

### 一、前言

1. [Spring Security(1) 入门体验](https://blog.csdn.net/qq_38225558/article/details/101754743)
2. [Spring Security(2) 自定义登录认证](https://blog.csdn.net/qq_38225558/article/details/102542072)
3. [Spring Security(3) 动态url权限控制](https://blog.csdn.net/qq_38225558/article/details/102637637)
4. [Spring Security(4) 整合JWT](https://zhengqing.blog.csdn.net/article/details/102701741)


### 二、什么是OAuth2？

OAuth 是一个开放标准，允许用户授权第三方应用访问他们在某网站上存储的私密资源（ex:用户昵称、头像等），在这个过程中无需将用户名和密码提供给第三方应用。

即以令牌token换资源信息数据。

### 三、应用场景

第三方应用授权登录，ex：QQ、微信授权登录

### 四、三部分

1. 第三方应用
2. 授权服务器
3. 资源服务器

### 五、四种授权模式

![](./images/20230912144041350.png)


#### 1. 授权码模式（authorization code）

> 常用模式，主流第三方验证

1. 第三方应用引导用户跳转到授权服务器的授权页面，授权后，授权服务器生成认证码code，然后携带code重定向返回
2. 第三方应用使用认证码code和自身应用凭证(app_id和app_secret)到授权服务器换取访问令牌(access_token)和更新令牌(refresh_token)
3. 第三方应用使用访问令牌(access_token)去资源服务器获取资源信息(ex:用户昵称，头像等)

#### 2. 简化模式（implicit）

> 适用于只有前端，前端单方面即可完成

1. 用户在第三方网站上点击授权跳转授权服务器，授权后，携带访问令牌(access_token)返回第三方网站
2. 第三方网站再携带 access_token 去资源服务器获取用户信息(昵称，头像等)

#### 3. 密码模式（resource owner password credentials）

> 适用于自家公司搭建的授权服务器，给兄弟公司用

直接使用用户名/密码从授权服务器获取访问令牌(access_token)

#### 4. 客户端模式（client credentials）

> 此过程中无需用户参与，一般用于提供给完全信任的服务端服务

客户端请求就返回访问令牌(access_token)

### 六、编程

> 参考 [https://mp.weixin.qq.com/mp/appmsgalbum?__biz=MzI1NDY0MTkzNQ==&action=getalbum&album_id=1319833457266163712](https://mp.weixin.qq.com/mp/appmsgalbum?__biz=MzI1NDY0MTkzNQ==&action=getalbum&album_id=1319833457266163712)
> `^_^`建议跟着这个大佬的OAuth2系列学习下`^_^`


![](./images/20230912144041465.png)

| 项目        | 端口 | 备注       |
| :---------- | :--- | :--------- |
| auth | 10010 | 授权服务器 |
| client  | 10020 | 第三方应用 |
| user | 10030 | 资源服务器 |

`oauth2.sql`

```sql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


-- 建库
create database if not exists oauth2 default charset = utf8mb4;
use oauth2;


-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`  (
     `client_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端ID，唯一标识',
     `client_secret` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端访问秘钥，BCryptPasswordEncoder加密算法加密',
     `resource_ids` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '可访问资源id(英文逗号分隔)',
     `scope` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '授权范围(英文逗号分隔)',
     `authorized_grant_types` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '授权类型(英文逗号分隔)',
     `web_server_redirect_uri` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '重定向uri',
     `authorities` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '@PreAuthorize(\"hasAuthority(\'admin\')\")可以在方法上标志 用户或者说client 需要说明样的权限\r\n\n\n指定客户端所拥有的Spring Security的权限值\r\n(英文逗号分隔)',
     `access_token_validity` int(11) NOT NULL COMMENT '令牌有效期(单位:秒)',
     `refresh_token_validity` int(11) NOT NULL COMMENT '刷新令牌有效期(单位:秒)',
     `additional_information` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预留字段,在Oauth的流程中没有实际的使用(JSON格式数据)',
     `autoapprove` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设置用户是否自动Approval操作, 默认值为 \'false\'\r\n可选值包括 \'true\',\'false\', \'read\',\'write\'.\r\n该字段只适用于grant_type=\"authorization_code\"的情况,当用户登录成功后,若该值为\'true\'或支持的scope值,则会跳过用户Approve的页面, 直接授权',
     `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES ('zq_app_id', '$2a$10$kdh16TzZoILQmaC4VkKdq.Ah2/u4LBbNJ7lpTqC8NXZVXKiu2LnVa', 'res1', 'all', 'authorization_code,refresh_token', 'http://127.0.0.1:10020/index.html', NULL, 3600, 259200, NULL, NULL, '2022-04-02 09:31:10', '2022-04-02 09:34:22');

SET FOREIGN_KEY_CHECKS = 1;
```

#### 1、授权服务

##### a、引入依赖

```xml
<!--Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- OAuth2 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-oauth2</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.security.oauth</groupId>
    <artifactId>spring-security-oauth2</artifactId>
    <!-- 指明版本，解决redis存储出现的问题：java.lang.NoSuchMethodError: org.springframework.data.redis.connection.RedisConnection.set([B[B)V -->
    <version>2.3.3.RELEASE</version>
</dependency>

<!-- 数据库相关 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.40</version>
</dependency>
```


##### b、`application.yml`配置

```yml
server:
  port: 10010

spring:
  application:
    name: auth
  # MySQL数据源配置
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/oauth2?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&useSSL=false # MySQL在高版本需要指明是否进行SSL连接 解决则加上 &useSSL=false
    username: root
    password: root
    driver-class-name: com.mysql.jdbc.Driver
```

##### c、Security 核心配置类

```java
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
```

##### d、配置生成token存储

```java
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
        // 原生jwt
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 自定义JWT返回信息
        JwtAccessTokenConverter converter = new MyJwt();
        converter.setSigningKey(this.SIGNING_KEY);
        return converter;
    }

}
```

##### e、自定义JWT返回信息

```java
public class MyJwt extends JwtAccessTokenConverter {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInformation = new LinkedHashMap<>();
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("author", "zhengqingya");
        info.put("gitee", "https://gitee.com/zhengqingya");
        info.put("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        additionalInformation.put("info", info);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
        return super.enhance(accessToken, authentication);
    }

}
```

##### f、配置添加JWT额外信息

```java
@Component
public class CustomAdditionalInformation implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> info = accessToken.getAdditionalInformation();
        info.put("author", "zhengqingya");
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }

}
```

##### j、授权服务器配置

```java
@Configuration
// 开启授权服务器的自动化配置
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Resource
    TokenStore tokenStore;

    @Resource
    ClientDetailsService clientDetailsService;

    @Resource
    DataSource dataSource;

    @Resource
    JwtAccessTokenConverter jwtAccessTokenConverter;

    @Resource
    CustomAdditionalInformation customAdditionalInformation;

    /**
     * {@link com.zhengqing.auth.config.SecurityConfig#authenticationManagerBean()}
     */
    @Resource
    AuthenticationManager authenticationManager;

    @Bean
    ClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(this.dataSource);
    }

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
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory()
//                // id
//                .withClient("zq_app_id")
//                // secret
//                .secret(new BCryptPasswordEncoder().encode("zq_app_secret"))
//                // 资源id
//                .resourceIds("res1")
//                // 授权类型 -- 授权码模式
//                .authorizedGrantTypes("authorization_code", "refresh_token")
//                // 授权范围
//                .scopes("all")
//                // 重定向uri
//                .redirectUris("http://127.0.0.1:10020/index.html");
//    }
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(this.jdbcClientDetailsService());
    }

    /**
     * 配置令牌的访问端点和令牌服务
     * 授权码和令牌有什么区别？授权码是用来获取令牌的，使用一次就失效，令牌则是用来获取资源的
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 配置授权码的存储
        endpoints.authorizationCodeServices(this.authorizationCodeServices())
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
//        services.setAccessTokenValiditySeconds(60 * 60 * 1);
        // 刷新token的有效期 -- 当token块过期的时候，需要获取一个新的token，在获取新token的时候，需要一个凭证信息，这个凭证信息不是旧的 Token，而是另外一个 refresh_token，这个 refresh_token 也是有有效期的。
//        services.setRefreshTokenValiditySeconds(60 * 60 * 24 * 3);

        // 注入“jwt添加额外信息”相关实例
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(this.jwtAccessTokenConverter, this.customAdditionalInformation));
        services.setTokenEnhancer(tokenEnhancerChain);

        return services;
    }

}
```

#### 2、资源服务

##### a、引入依赖

```xml
 <!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- OAuth2 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-oauth2</artifactId>
</dependency>
```

##### b、`application.yml`配置

```yml
server:
  port: 10030

spring:
  application:
    name: user
```

##### c、配置生成token存储

```java
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
```

##### d、资源服务器配置

```java
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Resource
    TokenStore tokenStore;

    /**
     * 配置一个 RemoteTokenServices 的实例，因为资源服务器和授权服务器是分开的；
     * 如果资源服务器和授权服务器是放在一起的，就不需要配置 RemoteTokenServices 了。
     * 校验客户端见 {@link com.zhengqing.auth.config.AuthorizationServerConfig#configure(org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer)}
     * <p>
     * 当用户来资源服务器请求资源时，会携带上一个 access_token，通过这里的配置，就能够校验出 token 是否正确等。
     */
//    @Bean
//    RemoteTokenServices tokenServices() {
//        RemoteTokenServices services = new RemoteTokenServices();
//        // access_token 的校验地址
//        services.setCheckTokenEndpointUrl("http://127.0.0.1:10010/oauth/check_token");
//        services.setClientId("zq_app_id");
//        services.setClientSecret("zq_app_secret");
//        return services;
//    }

    /**
     * 自动调用 JwtAccessTokenConverter 将 jwt 解析出来，jwt 里边就包含了用户的基本信息，所以就不用上面一样远程校验 access_token 了。
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("res1").tokenStore(this.tokenStore);
    }

    /**
     * 配置资源的拦截规则
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("admin")
                .anyRequest().authenticated();
    }
}
```

##### e、测试api

```java
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/admin/hello")
    public String admin() {
        return "admin";
    }
}
```

#### 3、第三方应用

##### a、引入依赖

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

##### b、`application.yml`配置

```yml
server:
  port: 10020

spring:
  application:
    name: client
```

##### c、index.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>APP</title>
</head>
<body>
Hello World! <br/>

<!--
    点击超链接实现第三方登录
    client_id 客户端 ID，根据我们在授权服务器中的实际配置填写。
    response_type 表示响应类型，这里是 code 表示响应一个授权码。
    redirect_uri 表示授权成功后的重定向地址，这里表示回到第三方应用的首页。
    scope 表示授权范围。
-->
<a href="http://127.0.0.1:10010/oauth/authorize?client_id=zq_app_id&response_type=code&scope=all&redirect_uri=http://127.0.0.1:10020/index.html">第三方登录</a>

<h1 th:text="${msg}"></h1>

</body>
</html>
```

##### d、测试api

```java
@Slf4j
@Controller
@RequestMapping("")
@Api(tags = {"测试api"})
public class TestController {

    @Resource
    private TokenTask tokenTask;

    @GetMapping("/index.html")
    public String hello(String code, Model model) {
        model.addAttribute("msg", this.tokenTask.getData(code));
        return "index";
    }

}
```

##### e、Token获取和定时刷新任务

```java
@Slf4j
@Component
@SessionScope
@EnableScheduling
public class TokenTask {

    public String access_token = "";
    public String refresh_token = "";

    public String getData(String code) {
        if (StringUtils.isBlank(code)) {
            return "未认证";
        }
        if (StringUtils.isBlank(this.access_token)) {
            /**
             * 如果 code 不为 null，标识是通过授权服务器重定向到这个地址来的
             * 根据拿到的 code 去获取 Token
             */
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("code", code);
            map.add("client_id", "zq_app_id");
            map.add("client_secret", "zq_app_secret");
            map.add("redirect_uri", "http://127.0.0.1:10020/index.html");
            map.add("grant_type", "authorization_code");
            Map<String, String> authResponseMap = new RestTemplate().postForObject("http://127.0.0.1:10010/oauth/token", map, Map.class);
            log.info("authResponse: {}", JSON.toJSONString(authResponseMap));

            this.access_token = authResponseMap.get("access_token");
            this.refresh_token = authResponseMap.get("refresh_token");

            return this.loadDataFromResServer();
        } else {
            return this.loadDataFromResServer();
        }
    }

    /**
     * 加载资源服务器数据
     */
    private String loadDataFromResServer() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + this.access_token);
            HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
            ResponseEntity<String> entity = new RestTemplate().exchange("http://127.0.0.1:10030/admin/hello", HttpMethod.GET, httpEntity, String.class);
            return entity.getBody();
        } catch (RestClientException e) {
            log.error("加载资源服务器数据:", e);
            return "未加载";
        }
    }

    /**
     * 刷新令牌定时任务
     * 每隔 55 分钟去刷新一下 access_token（access_token 有效期是 60 分钟）。
     */
//    @Scheduled(cron = "0 */55 * * * ?")
    @Scheduled(cron = "*/30 * * * * ?") // 每隔30秒
    public void tokenTask() {
        log.info("<<<<<< 刷新令牌定时任务 Start: 【{}】 >>>>>>", DateTime.now());
        if (StringUtils.isBlank(this.refresh_token)) {
            return;
        }
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", "zq_app_id");
        map.add("client_secret", "zq_app_secret");
        map.add("grant_type", "refresh_token");
        map.add("refresh_token", this.refresh_token);
        Map<String, String> authResponseMap = new RestTemplate().postForObject("http://127.0.0.1:10010/oauth/token", map, Map.class);
        log.info("刷新令牌定时任务 authResponse: {}", JSON.toJSONString(authResponseMap));

        this.access_token = authResponseMap.get("access_token");
        this.refresh_token = authResponseMap.get("refresh_token");

        // 测试解析用户信息
        this.checkToken();
    }

    /**
     * 解析jwt中的用户信息
     */
    private void checkToken() {
        if (StringUtils.isBlank(this.access_token)) {
            return;
        }
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("token", this.access_token);
        Map<String, String> authResponseMap = new RestTemplate().postForObject("http://127.0.0.1:10010/oauth/check_token", map, Map.class);
        log.info("解析jwt中的用户信息 authResponse: {}", JSON.toJSONString(authResponseMap));
    }

}
```

#### 4、测试 

访问 [http://127.0.0.1:10020/index.html](http://127.0.0.1:10020/index.html) 进行授权

这里贴出相关数据

普通token存储认证返回

```json
{
  "access_token": "b220ad2b-0907-47a7-b7a3-036f15f06834",
  "token_type": "bearer",
  "refresh_token": "7a1aefaa-8070-4bb4-8b15-e829a2584b12",
  "expires_in": 3775,
  "scope": "all"
}
```

---

token使用jwt存储认证返回

```json
{
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzMSJdLCJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJleHAiOjE2NDg4NzEyODAsImF1dGhvcml0aWVzIjpbIlJPTEVfYWRtaW4iXSwianRpIjoiZDI2YzAxZGItY2E4ZC00ODBjLTk4MzAtMzdkODk0M2U4YWY5IiwiY2xpZW50X2lkIjoienFfYXBwX2lkIn0.tzjoVFzsPjRPUPnsL3xaFVGENEBg2_V7bDEVk7EbfA0",
  "token_type": "bearer",
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzMSJdLCJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJhdGkiOiJkMjZjMDFkYi1jYThkLTQ4MGMtOTgzMC0zN2Q4OTQzZThhZjkiLCJleHAiOjE2NDkxMjY4MTIsImF1dGhvcml0aWVzIjpbIlJPTEVfYWRtaW4iXSwianRpIjoiNmFjZDQwYmYtYjk4Yi00Y2RkLWI4MDQtMTBlMzM1ZDNjY2YzIiwiY2xpZW50X2lkIjoienFfYXBwX2lkIn0.gnrtIKXhE71pJ1Fn5HmTZU-_3GW-Wst8lNrMplveipw",
  "expires_in": 3599,
  "scope": "all",
  "jti": "d26c01db-ca8d-480c-9830-37d8943e8af9",
  "author": "zhengqingya"
}
```

解析jwt中的用户信息，即上面的`access_token`

原生JWT返回信息

```json
{
  "aud": [
    "res1"
  ],
  "user_name": "admin",
  "scope": [
    "all"
  ],
  "active": true,
  "exp": 1648871280,
  "authorities": [
    "ROLE_admin"
  ],
  "jti": "d26c01db-ca8d-480c-9830-37d8943e8af9",
  "client_id": "zq_app_id"
}
```

自定义JWT返回信息

```json
{
  "aud": [
    "res1"
  ],
  "user_name": "admin",
  "scope": [
    "all"
  ],
  "active": true,
  "exp": 1648890210,
  "authorities": [
    "ROLE_admin"
  ],
  "jti": "d26c01db-ca8d-480c-9830-37d8943e8af9",
  "client_id": "zq_app_id",
  "info": {
    "author": "zhengqingya",
    "gitee": "https://gitee.com/zhengqingya",
    "user": {
      "username": "zq_app_id",
      "authorities": [],
      "accountNonExpired": true,
      "accountNonLocked": true,
      "credentialsNonExpired": true,
      "enabled": true
    }
  }
}
```

### 七、本文案例demo源码

[https://gitee.com/zhengqingya/java-workspace](https://gitee.com/zhengqingya/java-workspace)


--- 

> 今日分享语句：
> 你勤奋充电努力工作保持身材，对人微笑这些都不是为了取悦他人，而是为了扮靓自己照亮自己的心，告诉自己我是一股独立向上的力量。

