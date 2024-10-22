package com.zhengqing.spring.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RestController {

    /**
     * Bean name. Default to simple class name with first-letter-lowercase.
     */
    String value() default "";

}
