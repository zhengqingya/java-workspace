package com.zhengqing.common.base.constant;

/**
 * <p> 全局常用变量 - 安全认证 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/6/13 7:14 PM
 */
public interface SecurityConstant {

    /**
     * 认证请求头key
     */
    String AUTHORIZATION_KEY = "Authorization";

    /**
     * Basic认证前缀
     */
    String BASIC_PREFIX = "Basic ";

    /**
     * JWT令牌前缀
     */
    String JWT_PREFIX = "Bearer ";

    /**
     * JWT存储权限前缀
     */
    String AUTHORITY_PREFIX = "ROLE_";

    /**
     * JWT存储权限属性
     */
    String JWT_AUTHORITIES_KEY = "authorities";

    /**
     * 客户端ID
     */
    String CLIENT_ID_KEY = "client_id";

    /**
     * 刷新token
     */
    String REFRESH_TOKEN_KEY = "refresh_token";

    /**
     * 认证身份标识
     */
    String GRANT_TYPE = "authGrantType";

    /**
     * 超级管理员角色标识
     */
    String SUPER_ADMIN_ROLE_CODE = "super_admin";

    // ===============================================================================
    // ============================ ↓↓↓↓↓↓ redis缓存 ↓↓↓↓↓↓ ============================
    // ===============================================================================

    /**
     * jwt自定义用户信息
     */
    String JWT_CUSTOM_USER = "small-tools:system:jwt_custom_user:";

    /**
     * url权限关联角色
     * [ {接口路径:[角色编码]},...]
     */
    String URL_PERM_RE_ROLES = "small-tools:system:perm_rule:url";

    /**
     * 验证码
     */
    String CAPTCHA_CODE = "small-tools:auth:captcha:";

}
