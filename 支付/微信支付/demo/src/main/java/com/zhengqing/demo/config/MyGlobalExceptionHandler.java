package com.zhengqing.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

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

    @ExceptionHandler({Exception.class})
    public Object exception(Exception e) {
        log.error("error:", e);
        return new HashMap<String, Object>(2) {{
            this.put("code", 500);
            this.put("msg", e.getMessage());
        }};
    }

}
