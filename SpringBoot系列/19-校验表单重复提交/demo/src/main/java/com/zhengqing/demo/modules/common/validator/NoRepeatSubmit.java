package com.zhengqing.demo.modules.common.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> 注解：校验表单重复提交 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2019/11/27 9:59
 */
// 作用到方法上
@Target(ElementType.METHOD)
// 运行时有效
@Retention(RetentionPolicy.RUNTIME)
public @interface NoRepeatSubmit {
    /**
     * 默认时间3秒
     */
    int time() default 3 * 1000;
}
