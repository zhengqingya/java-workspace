package com.zhengqing.mybatis.plugin;

/**
 * <p> 拦截器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/26 01:25
 */
public interface Interceptor {

    Object intercept(Invocation invocation);

    <T> T plugin(Object target);

}
