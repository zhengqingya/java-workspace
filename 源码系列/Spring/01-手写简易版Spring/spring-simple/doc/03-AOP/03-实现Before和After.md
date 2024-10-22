### 实现Before和After

我们再继续思考，Spring提供的AOP拦截器，有Around、Before和After等好几种。

如何实现Before和After拦截？

实际上Around拦截本身就包含了Before和After拦截，我们没必要去修改`ProxyResolver`，只需要用Adapter模式提供两个拦截器模版，一个是`BeforeInvocationHandlerAdapter`：

```java
public abstract class BeforeInvocationHandlerAdapter implements InvocationHandler {

    public abstract void before(Object proxy, Method method, Object[] args);

    @Override
    public final Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before(proxy, method, args);
        return method.invoke(proxy, args);
    }
}
```

客户端提供的`InvocationHandler`只需继承自`BeforeInvocationHandlerAdapter`，自然就需要覆写`before()`方法，实现了Before拦截。

After拦截也是一个拦截器模版：

```java
public abstract class AfterInvocationHandlerAdapter implements InvocationHandler {
    // after 可以修改方法返回值
    public abstract Object after(Object proxy, Object returnValue, Method method, Object[] args);

    @Override
    public final Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = method.invoke(proxy, args);
        return after(proxy, result, method, args);
    }
}

```

### 扩展Annotation

截止目前，客户端只需要定义带有`@Around`注解的Bean，就能自动触发AOP。
我们思考下Spring的事务机制，其实也是AOP拦截，不过它的注解是`@Transactional`。
如果要扩展Annotation，即能自定义注解来启动AOP，怎么做？

假设我们后续编写了一个事务模块，提供注解`@Transactional`，那么，要启动AOP，就必须仿照`AroundProxyBeanPostProcessor`，提供一个`TransactionProxyBeanPostProcessor`，不过复制代码太麻烦了，我们可以改造一下`AroundProxyBeanPostProcessor`，用范型代码处理Annotation，先抽象出一个`AnnotationProxyBeanPostProcessor`：

```java
public abstract class AnnotationProxyBeanPostProcessor<A extends Annotation> implements BeanPostProcessor {

    Map<String, Object> originBeans = new HashMap<>();
    Class<A> annotationClass;

    public AnnotationProxyBeanPostProcessor() {
        this.annotationClass = getParameterizedType();
    }
    ...
}
```

实现`AroundProxyBeanPostProcessor`就一行定义：

```java
public class AroundProxyBeanPostProcessor extends AnnotationProxyBeanPostProcessor<Around> {
}
```

后续如果我们想实现`@Transactional`注解，只需定义：

```java
public class TransactionalProxyBeanPostProcessor extends AnnotationProxyBeanPostProcessor<Transactional> {
}
```

就能自动根据`@Transactional`启动AOP。
