# 实现Around

现在我们已经实现了ProxyResolver，下一步，实现完整的AOP就很容易了。

我们先从客户端代码入手，看看应当怎么装配AOP。

首先，客户端需要定义一个原始Bean，例如`OriginBean`，用`@Around`注解标注：

```java

@Component
@Around("aroundInvocationHandler")
public class OriginBean {
    @Value("jdbc.username")
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

`@Around`注解的值`aroundInvocationHandler`指出应该按什么名字查找拦截器，因此，客户端应再定义一个`AroundInvocationHandler`：

```java

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

```

有了原始Bean、拦截器，就可以在IoC容器中装配AOP：

```java

@Configuration
@ComponentScan
public class AroundApplication {

    @Bean
    AroundProxyBeanPostProcessor createAroundProxyBeanPostProcessor() {
        return new AroundProxyBeanPostProcessor();
    }
}

```

注意到装配AOP是通过`AroundProxyBeanPostProcessor`实现的，而这个类是由Framework提供，客户端并不需要自己实现。
因此，我们需要开发一个`AroundProxyBeanPostProcessor`：

```java
public class AroundProxyBeanPostProcessor implements BeanPostProcessor {

    Map<String, Object> originBeans = new HashMap<>();

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        // 检测@Around注解
        Around anno = beanClass.getAnnotation(Around.class);
        if (anno != null) {
            String handlerName;
            try {
                handlerName = (String) anno.annotationType().getMethod("value").invoke(anno);
            } catch (ReflectiveOperationException e) {
                throw new AopConfigException();
            }
            Object proxy = createProxy(beanClass, bean, handlerName);
            originBeans.put(beanName, bean);
            return proxy;
        } else {
            return bean;
        }
    }

    Object createProxy(Class<?> beanClass, Object bean, String handlerName) {
        ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) ApplicationContextUtils.getRequiredApplicationContext();
        BeanDefinition def = ctx.findBeanDefinition(handlerName);
        if (def == null) {
            throw new AopConfigException();
        }
        Object handlerBean = def.getInstance();
        if (handlerBean == null) {
            handlerBean = ctx.createBeanAsEarlySingleton(def);
        }
        if (handlerBean instanceof InvocationHandler handler) {
            return ProxyResolver.getInstance().createProxy(bean, handler);
        } else {
            throw new AopConfigException();
        }
    }

    @Override
    public Object postProcessOnSetProperty(Object bean, String beanName) {
        Object origin = this.originBeans.get(beanName);
        return origin != null ? origin : bean;
    }
}
```

上述`AroundProxyBeanPostProcessor`的机制非常简单：
检测每个Bean实例是否带有`@Around`注解，如果有，就根据注解的值查找Bean作为`InvocationHandler`，
最后创建Proxy，返回前保存了原始Bean的引用，因为IoC容器在后续的注入阶段要把相关依赖和值注入到原始Bean。

总结一下，目前功能包括：

- `Around`注解；
- `AroundProxyBeanPostProcessor`实现AOP。

客户端代码需要提供的包括：

- 带`@Around`注解的原始Bean；
- 实现`InvocationHandler`的Bean，名字与`@Around`注解value保持一致。
