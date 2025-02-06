# 通过代理工厂拿到UserMapper

mapper代理工厂

```java
import java.lang.reflect.Proxy;

public class MapperProxyFactory {
    /**
     * 拿到代理类
     */
    public static <T> T getProxy(Class<T> mapperClass) {
        /**
         * 第一个参数：类加载器
         * 第二个参数：增强方法所在的类，这个类实现的接口，表示这个代理类可以执行哪些方法。
         * 第三个参数：实现InvocationHandler接口，
         */
        return (T) Proxy.newProxyInstance(mapperClass.getClassLoader(), new Class[]{mapperClass}, new MapperProxy());
    }
}
```

mapper代理

```java
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MapperProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
```

通过代理工厂拿到UserMapper

```java
public class TestApp {
    @Test
    public void test() throws Exception {
        UserMapper userMapper = MapperProxyFactory.getProxy(UserMapper.class);
        List<User> userList = userMapper.selectList();
        System.out.println(userList);
    }
}
```
