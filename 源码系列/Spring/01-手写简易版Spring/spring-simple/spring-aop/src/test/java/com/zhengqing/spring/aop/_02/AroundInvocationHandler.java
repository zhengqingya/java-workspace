package com.zhengqing.spring.aop._02;

import com.zhengqing.spring.annotation.Component;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
@Component
public class AroundInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 记录标记@Log方法的日志信息
        if (method.getAnnotation(Log.class) != null) {
            String result = (String) method.invoke(proxy, args);
            log.debug("您调用了方法：{} 方法返回值：{}", method.getName(), result);
            return result;
        }
        return method.invoke(proxy, args);
    }
}
