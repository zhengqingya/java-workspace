package com.zhengqing.demo.modules.common.validator.textformat;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * <p> 自定义参数校验注解： @TextFormat </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2019/12/11 11:00
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TextFormatValidatorClass.class)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.TYPE})
public @interface TextFormat {

    /**
     * 判断字符串中是否包含中文 包含则抛出异常
     */
    boolean notChinese() default false;

    /**
     * 是否包含
     */
    String[] contains() default {};

    /**
     * 是否不包含
     */
    String[] notContains() default {};

    /**
     * 前缀以xx开始
     */
    String startWith() default "";

    /**
     * 后缀以xx结束
     */
    String endsWith() default "";

    /**
     * 默认错误提示信息
     */
    String message() default "参数校验失败!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
