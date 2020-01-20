package com.zhengqing.demo.modules.common.validator.response;

import com.zhengqing.demo.modules.common.validator.textformat.TextFormatValidatorClass;

import javax.validation.Constraint;
import java.lang.annotation.*;

/**
 * <p> 自定义接口返回值注解： @ResponseResult </p>
 *
 * @author : zhengqing
 * @description : 该注解运用在方法、类、接口、枚举上...
 * @date : 2019/12/24 11:31
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TextFormatValidatorClass.class)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ResponseResult {

}
