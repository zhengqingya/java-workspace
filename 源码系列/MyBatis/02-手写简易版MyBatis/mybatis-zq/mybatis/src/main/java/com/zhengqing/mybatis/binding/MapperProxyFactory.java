package com.zhengqing.mybatis.binding;

import com.zhengqing.mybatis.session.SqlSession;

import java.lang.reflect.Proxy;

/**
 * <p> mapper代理工厂 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/21 19:57
 */
public class MapperProxyFactory {

    /**
     * 拿到代理类
     */
    public static <T> T getProxy(Class<T> mapperClass, SqlSession sqlSession) {
        /**
         * 第一个参数：类加载器
         * 第二个参数：增强方法所在的类，这个类实现的接口，表示这个代理类可以执行哪些方法。
         * 第三个参数：实现InvocationHandler接口，
         */
        return (T) Proxy.newProxyInstance(mapperClass.getClassLoader(), new Class[]{mapperClass}, new MapperProxy(sqlSession, mapperClass));
    }

}
