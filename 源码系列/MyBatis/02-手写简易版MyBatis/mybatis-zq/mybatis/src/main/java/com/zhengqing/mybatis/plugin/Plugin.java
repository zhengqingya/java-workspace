package com.zhengqing.mybatis.plugin;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Set;

/**
 * <p> 插件代理 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/26 01:09
 */
public class Plugin implements InvocationHandler {

    private Object target;
    private Interceptor interceptor;
    private Set<Method> methods;

    public Plugin(Object target, Interceptor interceptor, Set<Method> methods) {
        this.target = target;
        this.interceptor = interceptor;
        this.methods = methods;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (this.methods != null && this.methods.contains(method)) {
            return this.interceptor.intercept(new Invocation(this.target, method, args));
        } else {
            return method.invoke(this.target, args);
        }
    }

    public static <T> T wrap(Object target, Interceptor interceptor) {
        Map<Class<?>, Set<Method>> signatureMap = getSignatureMap(interceptor);
        boolean isProxy = false;
        Set<Method> methods = null;
        for (Class<?> aClass : signatureMap.keySet()) {
            if (aClass.isAssignableFrom(target.getClass())) {
                isProxy = true;
                methods = signatureMap.get(aClass);
                break;
            }
        }

        if (isProxy) {
            // 需要代理
            return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new Plugin(target, interceptor, methods));
        } else {
            // 不需要代理
            return (T) target;
        }
    }

    @SneakyThrows
    private static Map<Class<?>, Set<Method>> getSignatureMap(Interceptor interceptor) {
        // class -> query,update
        Map<Class<?>, Set<Method>> result = Maps.newHashMap();
        Intercepts intercepts = interceptor.getClass().getAnnotation(Intercepts.class);
        Signature[] signatures = intercepts.value();
        for (Signature signature : signatures) {
            Class<?> type = signature.type();
            String methodName = signature.method();
            Class<?>[] args = signature.args();
            Method method = type.getMethod(methodName, args);
            Set<Method> methods = result.get(type);
            if (methods == null) {
                result.put(type, Sets.newHashSet(method));
            } else {
                methods.add(method);
            }
        }
        return result;
    }

}
