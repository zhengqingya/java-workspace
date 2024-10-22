package com.zhengqing.spring.aop._01;

import com.zhengqing.spring.aop.ProxyResolver;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

@Slf4j
public class TestProxyResolver {

    @Test
    public void test() {
        OriginBean origin = new OriginBean();
        origin.name = "admin";
        System.out.println(origin.hello());

        // create proxy
        OriginBean proxy = ProxyResolver.getInstance().createProxy(origin, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 记录标记@Log方法的日志信息
                if (method.getAnnotation(Log.class) != null) {
                    String result = (String) method.invoke(origin, args);
                    log.debug("您调用了方法：{} 方法返回值：{}", method.getName(), result);
                    return result;
                }
                return method.invoke(origin, args);
            }
        });

        // Proxy类名
        System.out.println(proxy.getClass().getName());

        // Proxy类与OriginBean.class不同
        assertNotSame(OriginBean.class, proxy.getClass());
        // proxy实例的name字段应为null
        assertNull(proxy.name);

        System.out.println(proxy.hello());
        System.out.println(proxy.hi());
    }

}
