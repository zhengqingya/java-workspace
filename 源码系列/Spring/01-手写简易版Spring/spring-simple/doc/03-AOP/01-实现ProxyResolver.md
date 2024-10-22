# 实现ProxyResolver

为了实现AOP，我们先思考如何在IoC容器中实现一个动态代理。

在IoC容器中，实现动态代理需要用户提供两个Bean：

1. 原始Bean，即需要被代理的Bean；
2. 拦截器，即拦截了目标Bean的方法后，会自动调用拦截器实现代理功能。

拦截器需要定义接口，这里我们直接用Java标准库的`InvocationHandler`，免去了自定义接口。

假定我们已经从IoC容器中获取了原始Bean与实现了`InvocationHandler`的拦截器Bean，那么就可以编写一个`ProxyResolver`来实现AOP代理。

```xml
<!-- https://mvnrepository.com/artifact/net.bytebuddy/byte-buddy -->
<dependency>
    <groupId>net.bytebuddy</groupId>
    <artifactId>byte-buddy</artifactId>
    <version>1.15.4</version>
</dependency>
```

```java
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class ProxyResolver {

    // ByteBuddy实例
    final ByteBuddy byteBuddy = new ByteBuddy();

    /**
     * 传入原始Bean、拦截器，返回代理后的实例
     */
    @SuppressWarnings("unchecked")
    public <T> T createProxy(T bean, InvocationHandler handler) {
        // 目标Bean的Class类型
        Class<?> targetClass = bean.getClass();
        // 动态创建Proxy的Class
        Class<?> proxyClass = this.byteBuddy
                // 子类用默认无参数构造方法
                .subclass(targetClass, ConstructorStrategy.Default.DEFAULT_CONSTRUCTOR)
                // 拦截所有public方法
                .method(ElementMatchers.isPublic()).intercept(InvocationHandlerAdapter.of(
                        // 新的拦截器实例
                        new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                // 将方法调用代理至原始Bean
                                return handler.invoke(bean, method, args);
                            }
                        }))
                // 生成字节码
                .make()
                // 加载字节码
                .load(targetClass.getClassLoader()).getLoaded();
        // 创建Proxy实例:
        Object proxy;
        try {
            proxy = proxyClass.getConstructor().newInstance();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return (T) proxy;
    }
}
```

就这样几行代码实现了AOP功能。

---

测试看看：实现AOP功能，增强带`@Log`注解的方法，记录调用日志

```java

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
}
```

```java
public class OriginBean {
    public String name;

    @Log
    public String hello() {
        return "Hello ..." + name;
    }

    public String hi() {
        return "Hi ..." + name;
    }
}
```

```java

@Test
public void test() {
    OriginBean origin = new OriginBean();
    origin.name = "admin";
    System.out.println(origin.hello());

    // create proxy
    OriginBean proxy = new ProxyResolver().createProxy(origin, new InvocationHandler() {
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
```
