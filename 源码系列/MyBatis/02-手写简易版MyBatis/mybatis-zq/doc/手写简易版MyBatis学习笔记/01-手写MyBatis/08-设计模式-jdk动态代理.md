# 设计模式-jdk动态代理

JDK动态代理是Java标准库中的一种动态代理机制，它允许你在运行时创建一个实现了一组给定接口的新类的实例。以下是JDK动态代理的一些关键点：

- **基于接口的代理**：
    - JDK动态代理要求被代理的类必须实现至少一个接口。代理类会实现这些接口，并在运行时生成代理对象。

- **核心类**：
    - `java.lang.reflect.Proxy`：用于创建动态代理类的实例。
    - `java.lang.reflect.InvocationHandler`：代理对象的方法调用会被转发到`InvocationHandler`的`invoke`方法中。

- **创建代理对象的步骤**：
    1. **定义接口**：被代理类必须实现一个或多个接口。
    2. **实现InvocationHandler**：创建一个实现`InvocationHandler`接口的类，并在`invoke`方法中编写代理逻辑。
    3. **生成代理对象**：使用`Proxy.newProxyInstance`方法生成代理对象。

- **优点**：
    - 灵活性高，可以在运行时创建代理对象。
    - 无需为每个被代理类编写单独的代理类，减少了代码量。

- **缺点**：
    - 被代理类必须实现接口，否则无法使用JDK动态代理。
    - 性能相对较低，因为方法调用会经过`InvocationHandler`的`invoke`方法。

- **适用场景**：
    - 适用于需要在运行时动态地创建代理对象，并且被代理类实现了接口的场景。

### 示例代码

#### 1、用户service

```java
public interface UserService {
    Object selectList(String name);

    Object selectOne(String name);
}
```

#### 2、用户实现类

```java
public class UserServiceImpl implements UserService {
    @Override
    public Object selectList(String name) {
        System.out.println("执行了selectList: " + name);
        return "ok";
    }

    @Override
    public Object selectOne(String name) {
        System.out.println("执行了selectOne: " + name);
        return "ok";
    }
}
```

#### 3、jdk动态代理

```java
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
}
```

#### 4、测试

```java
public static void main(String[] args) {
    JdkProxy jdkProxy = new JdkProxy(new UserServiceImpl());
    UserService userService = jdkProxy.getProxy(UserService.class);
    System.out.println(userService.selectOne("xx"));
    System.out.println(userService.selectList("666"));
}
```

输出结果：

```shell
invoke before ...
执行了selectOne: xx
invoke after ...
ok
invoke before ...
执行了selectList: 666
invoke after ...
ok
```