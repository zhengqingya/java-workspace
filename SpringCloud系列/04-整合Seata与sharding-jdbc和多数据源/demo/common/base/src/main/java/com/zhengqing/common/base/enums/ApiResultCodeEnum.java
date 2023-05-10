package com.zhengqing.common.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 响应码枚举 - 可参考HTTP状态码的语义
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2019/8/22 11:09
 */
@Getter
@AllArgsConstructor
public enum ApiResultCodeEnum {

    // 成功
    SUCCESS(200, "SUCCESS"),
    // 失败
    FAILURE(400, "FAILURE"),
    // 参数校验失败
    PARAM_VALID_ERROR(400, "参数校验失败"),
    // 未登录
    UN_LOGIN(401, "请求未授权"),
    // 未通过认证
    USER_UNAUTHORIZED(402, "用户名或密码不正确"),
    // 用户不存在
    USER_NOT_EXIST(402, "用户不存在"),
    // 未认证（签名错误、token错误）
    UNAUTHORIZED(403, "未认证"),
    // 客户端认证失败
    CLIENT_AUTHENTICATION_FAILED(405, "客户端认证失败"),
    // 接口不存在
    NOT_FOUND(404, "接口不存在"),
    // token过期
    TOKEN_EXPIRED(-1, "token过期"),
    // token丢失
    TOKEN_NOT_EXIST(-1, "token丢失"),
    // token已被禁止访问
    TOKEN_ACCESS_FORBIDDEN(-1, "token已被禁止访问"),
    // 服务器内部错误
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    ;

    private final int code;
    private final String desc;

}
