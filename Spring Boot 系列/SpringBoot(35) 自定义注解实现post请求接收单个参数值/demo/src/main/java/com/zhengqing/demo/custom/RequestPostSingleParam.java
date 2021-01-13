package com.zhengqing.demo.custom;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.ValueConstants;

/**
 * <p>
 * 自定义注解`RequestPostSingleParam` - 处理接收单个参数的`post`请求
 * </p>
 *
 * @author : zhengqing
 * @description : 使用时注意，用了`RequestPostSingleParam`接收参数就只能作用于一个参数，不能在controller层方法中写多个参数了！！！
 * @date : 2021/1/13 14:41
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestPostSingleParam {

    /**
     * Alias for {@link #name}.
     */
    @AliasFor("name")
    String value() default "";

    /**
     * The name of the request parameter to bind to.
     *
     * @since 4.2
     */
    @AliasFor("value")
    String name() default "";

    /**
     * Whether the parameter is required.
     * <p>
     * Defaults to {@code true}, leading to an exception being thrown if the parameter is missing in the request. Switch
     * this to {@code false} if you prefer a {@code null} value if the parameter is not present in the request.
     * <p>
     * Alternatively, provide a {@link #defaultValue}, which implicitly sets this flag to {@code false}.
     */
    boolean required() default true;

    /**
     * The default value to use as a fallback when the request parameter is not provided or has an empty value.
     * <p>
     * Supplying a default value implicitly sets {@link #required} to {@code false}.
     */
    String defaultValue() default ValueConstants.DEFAULT_NONE;

}
