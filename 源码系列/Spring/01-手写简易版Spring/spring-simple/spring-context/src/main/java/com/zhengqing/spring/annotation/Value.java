package com.zhengqing.spring.annotation;

import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {

    String value();
}
