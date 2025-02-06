package com.zhengqing.mybatis.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> 插件拦截器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/20 19:21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Signature {

    Class<?> type(); // 拦截到的类

    String method(); // 拦截到的方法

    Class<?>[] args(); // 拦截的方法参数

}
