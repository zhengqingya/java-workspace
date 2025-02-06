package com.zhengqing.mybatis.design.proxy._03;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * <p> cglib动态代理 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/21 19:33
 */
public class CglibProxy implements MethodInterceptor {

    public Object getProxy(Class<?> clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz); // 被代理的目标类
        enhancer.setCallback(this); // 拦截器
        return enhancer.create(); // 创建代理类
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("cglib动态代理开始");
        Object result = methodProxy.invokeSuper(o, args);
        System.out.println("cglib动态代理结束");
        return result;
    }

    public static void main(String[] args) {
        CglibProxy cglibProxy = new CglibProxy();
        UserService proxy = (UserService) cglibProxy.getProxy(UserService.class);
        System.out.println(proxy.selectList("zq"));
    }

}
