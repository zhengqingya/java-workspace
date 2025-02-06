package com.zhengqing.mybatis.spring.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> mapper包扫描 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/5/5 05:01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(MapperScannerRegistrar.class)
public @interface MapperScan {

    String value();

}
