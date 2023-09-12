# 说明

> 参考 https://mp.weixin.qq.com/mp/appmsgalbum?__biz=MzI1NDY0MTkzNQ==&action=getalbum&album_id=1319833457266163712

### 工程说明

| 工程               | 备注       |
| :----------------- | :--------- |
| authorization_code | 授权码模式 |
| implicit           | 简化模式   |
| password           | 密码模式   |
| client_credentials | 客户端模式 |
| authorization_code_redis | 令牌存储到redis中 |
| authorization_code_jwt | access_token替换成jwt |
| authorization_code_jwt_sso | 单点登录`@EnableOAuth2Sso` |
| gitee_login | Gitee三方登录 |

### 子项目说明

| 项目        | 端口 | 备注       |
| :---------- | :--- | :--------- |
| auth | 10010 | 授权服务器 |
| client  | 10020 | 第三方应用 |
| user | 10030 | 资源服务器 |

访问 http://127.0.0.1:10020/index.html 进行授权

### 授权码模式

http://127.0.0.1:10010/oauth/authorize?client_id=zq_app_id&response_type=code&scope=all&redirect_uri=http://127.0.0.1:10020/index.html

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

### jwt

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

### sql

> 可参考 https://github.com/spring-projects/spring-security-oauth/blob/main/spring-security-oauth2/src/test/resources/schema.sql

见 [oauth2.sql](./sql/oauth2.sql)

### 其它

auth暴露接口

![](images/auth暴露接口.png)

| 端点                  | 备注                                                         |
| :-------------------- | :----------------------------------------------------------- |
| /oauth/authorize      | 授权的端点                                             |
| /oauth/token          | 用来获取令牌的端点                                     |
| /oauth/confirm_access | 用户确认授权提交的端点（auth 询问用户是否授权那个页面的提交地址） |
| /oauth/error          | 授权出错的端点                                               |
| /oauth/check_token    | 校验 access_token 的端点                                     |
| /oauth/token_key      | 提供公钥的端点                                               |

`/oauth/token` 端点除了颁发令牌，也可刷新令牌

![](images/auth刷新令牌.png)
