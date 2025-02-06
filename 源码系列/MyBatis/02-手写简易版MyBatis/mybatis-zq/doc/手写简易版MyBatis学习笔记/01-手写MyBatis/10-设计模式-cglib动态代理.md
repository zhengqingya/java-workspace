# 设计模式-cglib动态代理

CGLIB（Code Generation Library）动态代理是一种强大的代理机制，它允许你在运行时创建一个子类来代理目标类。与JDK动态代理不同，CGLIB代理不需要目标类实现接口，因此它适用于那些没有实现接口的类。以下是CGLIB动态代理的一些关键点：

### 主要功能

1. **代理未实现接口的类**：
    - CGLIB可以代理任何类，即使该类没有实现任何接口。
2. **生成子类**：
    - CGLIB通过生成目标类的子类来实现代理，因此可以代理所有非final的方法。
3. **方法拦截**：
    - 可以在方法调用前后添加额外的逻辑，例如日志记录、权限检查、事务管理等。

### 优点

- **灵活性**：
    - 可以代理任何类，包括没有实现接口的类。
- **性能**：
    - 相对于JDK动态代理，CGLIB的性能通常更好，因为它直接操作字节码。

### 缺点

- **依赖第三方库**：
    - 需要引入CGLIB库，增加了项目的依赖。
- **无法代理final类和方法**：
    - 由于CGLIB通过继承来实现代理，因此无法代理`final`类和`final`方法。
- **复杂性**：
    - 相对于JDK动态代理，CGLIB的使用稍微复杂一些。

### 示例代码

以下是一个简单的CGLIB动态代理示例，展示了如何使用CGLIB来创建代理对象并拦截方法调用：

#### 1、用户service

```java
public class UserService {
    public Object selectList(String name) {
        System.out.println("查询用户列表");
        return "ok";
    }
}
```

#### 2、cglib动态代理

```java
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

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
```

输出结果：

```shell
cglib动态代理开始
查询用户列表
cglib动态代理结束
ok
```

在`MethodInterceptor`实现类的`intercept`方法中，我们可以在方法调用前后添加额外的逻辑。
通过这种方式，CGLIB动态代理可以在运行时动态地创建代理对象并拦截方法调用。
