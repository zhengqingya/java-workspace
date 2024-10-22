package com.zhengqing.spring.annotation;


import com.zhengqing.spring.webmvc.utils.WebUtils;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {

    String value();

    String defaultValue() default WebUtils.DEFAULT_PARAM_VALUE;
}
