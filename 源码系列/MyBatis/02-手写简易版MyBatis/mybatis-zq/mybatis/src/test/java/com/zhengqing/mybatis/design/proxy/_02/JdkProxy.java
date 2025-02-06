package com.zhengqing.mybatis.design.proxy._02;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <p> jdk动态代理 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/21 18:29
 */
public class JdkProxy implements InvocationHandler {

    private Object target;

    public JdkProxy(Object target) {
        this.target = target;
    }

    /**
     * 拿到代理类
     */
    public <T> T getProxy(Class<T> clz) {
        /**
         * 第一个参数：类加载器
         * 第二个参数：增强方法所在的类，这个类实现的接口，表示这个代理类可以执行哪些方法。
         * 第三个参数：实现InvocationHandler接口，
         */
        return (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{clz}, this);
    }

    /**
     * 方法增强
     *
     * @param proxy  代理对象
     * @param method 执行方法
     * @param args   执行方法携带的参数
     * @return java.lang.Object
     * @author zhengqingya
     * @date 2024/4/21 18:37
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("invoke before ...");
        Object result = method.invoke(this.target, args);
        System.out.println("invoke after ...");
        return result;
    }

    public static void main(String[] args) {
        JdkProxy jdkProxy = new JdkProxy(new UserServiceImpl());
        UserService userService = jdkProxy.getProxy(UserService.class);
        System.out.println(userService.selectOne("xx"));
        System.out.println(userService.selectList("666"));
    }


}
