package com.zhengqing.demo.modules.common.enumeration;


/**
 * <p> 响应码枚举 - 可参考HTTP状态码的语义 </p>
 *
 * @author : zhengqing
 * @description : HTTP状态码总的分为五类：
 * 1开头：信息状态码
 * 2开头：成功状态码
 * 3开头：重定向状态码
 * 4开头：客户端错误状态码
 * 5开头：服务端错误状态码
 * @date : 2019/8/22 11:09
 */
public enum ResultCode {
    SUCCESS(200, "SUCCESS"),
    FAILE(-1, "FAILE"),
    PARAM_ERROR(201, "参数不合法"),
    UN_LOGIN(401, "未登录"),
    USER_UNAUTHORIZED(402, "用户名或密码不正确"),
    UNAUTHORIZED(403, "未认证或Token失效"),
    NOT_FOUND(404, "接口不存在"),
    SYSTEM_UNKNOWN_ERROR(500, "服务器内部错误");

    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
