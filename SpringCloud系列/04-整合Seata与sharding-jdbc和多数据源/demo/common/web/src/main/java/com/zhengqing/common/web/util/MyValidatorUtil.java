package com.zhengqing.common.web.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.validation.*;
import java.util.Set;

/**
 * <p>
 * 校验工具类
 * </p>
 *
 * @author zhengqingya
 * @date 2019/9/10 9:28
 */
@Slf4j
public class MyValidatorUtil {

    /**
     * 手动校验 (把注解@valid放在servevice层上是没有效果的，只有放在Controller上才有效果,因此通过调用ValidatorFactory工厂方法创建一个实例对象来进行手动校验 )
     * 百度建议： @Validated放到实现上,@NotNull,@Valid等声明放到接口上
     *
     * @param obj    校验对象
     * @param groups 组校验
     * @return void
     * @author zhengqingya
     * @date 2020/10/19 11:27
     */
    @SneakyThrows(Exception.class)
    public static void validate(Object obj, Class<?>... groups) {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        Set<ConstraintViolation<Object>> set = validator.validate(obj, groups);
        for (ConstraintViolation<Object> constraintViolation : set) {
            String violationMessage = constraintViolation.getMessage();
            log.error(" 【{}】", constraintViolation.getPropertyPath() + ":" + violationMessage);
            throw new ValidationException(violationMessage);
        }
    }

}
