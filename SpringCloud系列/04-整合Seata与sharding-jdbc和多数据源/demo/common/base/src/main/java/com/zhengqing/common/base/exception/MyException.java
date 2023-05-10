package com.zhengqing.common.base.exception;

import com.zhengqing.common.base.enums.ApiResultCodeEnum;

/**
 * <p>
 * 自定义异常类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2019/8/26 15:11
 */
public class MyException extends RuntimeException {

    /**
     * 异常状态码
     */
    private Integer code;

    public MyException(Throwable cause) {
        super(cause);
    }

    public MyException(String message) {
        super(message);
    }

    public MyException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public MyException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public MyException(ApiResultCodeEnum response) {
        super(response.getDesc());
        this.code = response.getCode();
    }

    public MyException(String message, Throwable cause) {
        super(message, cause);
    }

    public Integer getCode() {
        return code;
    }

}
