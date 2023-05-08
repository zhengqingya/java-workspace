package com.zhengqing.demo.config.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.util.SaResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>
 * 全局异常处理器
 * </p>
 *
 * @author zhengqingya
 * @description 在spring 3.2中，新增了@ControllerAdvice
 * 注解，可以用于定义@ExceptionHandler、@InitBinder、@ModelAttribute，并应用到所有@RequestMapping中
 * @date 2019/8/25 0025 18:56
 */
@Slf4j
@RestControllerAdvice
public class MyGlobalExceptionHandler {

    @ExceptionHandler({SaTokenException.class})
    public SaResult saTokenException(SaTokenException e) {
        log.error("SaTokenException: ", e);
        return SaResult.error(e.getMessage());
    }

    @ExceptionHandler({NotLoginException.class})
    public SaResult notLoginException(NotLoginException e) {
        log.error("NotLoginException: ", e);
        return SaResult.error(e.getMessage());
    }

}
