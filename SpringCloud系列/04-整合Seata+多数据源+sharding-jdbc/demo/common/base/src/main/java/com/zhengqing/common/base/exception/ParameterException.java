package com.zhengqing.common.base.exception;

/**
 * <p>
 * 参数异常
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/8/1 18:07
 */
public class ParameterException extends MyException {

    public ParameterException(String message) {
        super(message);
    }

    public ParameterException(String message, Throwable e) {
        super(message, e);
    }

}
