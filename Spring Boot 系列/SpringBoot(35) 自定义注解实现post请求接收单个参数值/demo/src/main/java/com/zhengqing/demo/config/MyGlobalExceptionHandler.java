package com.zhengqing.demo.config;

import java.util.Map;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 全局异常处理器
 * </p>
 *
 * @description: 在spring 3.2中，新增了@ControllerAdvice
 *               注解，可以用于定义@ExceptionHandler、@InitBinder、@ModelAttribute，并应用到所有@RequestMapping中
 * @author: zhengqing
 * @date: 2019/8/25 0025 18:56
 */
@Slf4j
@RestControllerAdvice
public class MyGlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    public Map<String, Object> exception(Exception e) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("code", 500);
        map.put("msg", e.getMessage());
        return map;
    }

}
