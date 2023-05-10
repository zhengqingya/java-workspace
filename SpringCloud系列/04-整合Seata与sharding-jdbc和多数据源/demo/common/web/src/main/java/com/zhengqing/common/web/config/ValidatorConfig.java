package com.zhengqing.common.web.config;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * <p> 校验配置 </p>
 *
 * @author zhengqingya
 * @description 快速失败： Spring Validation默认会校验完所有字段，然后才抛出异常。可以通过一些简单的配置，开启`Fail Fast`模式，一旦校验失败就立即返回。
 * @date 2021/12/16 9:30
 */
@Slf4j
@Component
public class ValidatorConfig {

    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                // 快速失败模式
                .failFast(true)
                .buildValidatorFactory();
        log.info("Spring Validation 快速失败模式开启 ...");
        return validatorFactory.getValidator();
    }

}
