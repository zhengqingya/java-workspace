# 实现BeanPostProcessor

现在，我们已经完成了扫描Class名称、创建BeanDefinition、创建Bean实例、初始化Bean，理论上一个可用的IoC容器就已经就绪。

`BeanPostProcessor`：是 Spring 框架中的一个接口，它允许开发者在 Bean 初始化前后执行自定义的操作。
这个接口主要用于拦截 Bean 的创建过程，可以用来修改 Bean 的定义或者对 Bean 进行增强。

我们举个例子，基于Spring代码：

```java
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Configuration
@ComponentScan
public class AppConfig {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        // 可以获取到ZonedDateTime
        ZonedDateTime dt = ctx.getBean(ZonedDateTime.class);
        System.out.println(dt);
        // 错误:NoSuchBeanDefinitionException
        System.out.println(ctx.getBean(LocalDateTime.class));
    }

    // 创建LocalDateTime实例
    @Bean
    public LocalDateTime localDateTime() {
        return LocalDateTime.now();
    }

    // 实现一个BeanPostProcessor
    @Bean
    BeanPostProcessor replaceLocalDateTime() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                // 将LocalDateTime类型实例替换为ZonedDateTime类型实例
                if (bean instanceof LocalDateTime) {
                    return ZonedDateTime.now();
                }
                return bean;
            }
        };
    }
}
```

运行可知，我们定义的`@Bean`类型明明是`LocalDateTime`类型，但却被另一个`BeanPostProcessor`替换成了`ZonedDateTime`，于是，调用`getBean(ZonedDateTime.class)`可以拿到替换后的Bean，调用`getBean(LocalDateTime.class)`会报错，提示找不到Bean。
那么原始的Bean哪去了？答案是被`BeanPostProcessor`扔掉了。

可见，`BeanPostProcessor`是一种特殊Bean，它的作用是根据条件替换某些Bean。
上述的例子中，`LocalDateTime`被替换为`ZonedDateTime`其实没啥意义，但实际应用中，把原始Bean替换为代理后的Bean是非常常见的，比如下面的基于Spring的代码：

```java
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Configuration
@ComponentScan
public class AppConfig {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService u = ctx.getBean(UserService.class);
        System.out.println(u.getClass().getSimpleName()); // UserServiceProxy
        u.register("admin", "123456");
    }

    // 创建LocalDateTime实例
    @Bean
    public LocalDateTime localDateTime() {
        return LocalDateTime.now();
    }

    // 实现一个BeanPostProcessor
    @Bean
    BeanPostProcessor replaceLocalDateTime() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                // 实现事务功能
                if (bean instanceof UserService) {
                    return new UserServiceProxy((UserService) bean);
                }
                return bean;
            }
        };
    }
}

@Component
class UserService {
    public void register(String name, String password) {
        System.out.println("INSERT INTO ...");
    }
}

// 代理类
class UserServiceProxy extends UserService {
    UserService target;

    public UserServiceProxy(UserService target) {
        this.target = target;
    }

    @Override
    public void register(String name, String password) {
        System.out.println("begin tx");
        target.register(name, password);
        System.out.println("commit tx");
    }
}
```

如果执行上述代码，打印出的Bean类型不是`UserService`，而是`UserServiceProxy`，因此，调用`register()`会打印出`begin tx`和`commit tx`，说明“事务”生效了。

迄今为止，创建Proxy似乎没有什么影响。让我们把代码再按实际情况扩展一下，`UserService`是用户编写的业务代码，需要注入`JdbcTemplate`：

```java

@Component
class UserService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void register(String name, String password) {
        jdbcTemplate.update("INSERT INTO ...");
    }
}
```

而`PostBeanProcessor`一般由框架本身提供事务功能，所以它会动态创建一个`UserServiceProxy`：

```java
class UserServiceProxy extends UserService {
    UserService target;

    public UserServiceProxy(UserService target) {
        this.target = target;
    }

    @Override
    public void register(String name, String password) {
        System.out.println("begin tx");
        target.register(name, password);
        System.out.println("commit tx");
    }
}
```

调用用户注册的页面由`MvcController`控制，因此，将`UserService`注入到`MvcController`：

```java

@Controller
class MvcController {
    @Autowired
    UserService userService;

    @PostMapping("/register")
    void register() {
        userService.register(...);
    }
}
```

一开始，由IoC容器创建的Bean包括：

- JdbcTemplate
- UserService
- MvcController

接着，由于`BeanPostProcessor`的介入，原始的`UserService`被替换为`UserServiceProxy`：

- JdbcTemplate
- UserServiceProxy
- MvcController

那么问题来了：注意到`UserServiceProxy`是从`UserService`继承的，它也有一个`@Autowired JdbcTemplate`，那`JdbcTemplate`实例应注入到原始的`UserService`还是`UserServiceProxy`？

从业务逻辑出发，`JdbcTemplate`实例必须注入到原始的`UserService`，否则，代理类`UserServiceProxy`执行`target.register()`时，相当于对原始的`UserService`调用`register()`方法，如果`JdbcTemplate`没有注入，将直接报`NullPointerException`错误。

这时第二个问题又来了：`MvcController`需要注入的`UserService`，应该是原始的`UserService`还是`UserServiceProxy`？

还是从业务逻辑出发，`MvcController`需要注入的`UserService`必须是`UserServiceProxy`，否则，事务不起作用。

我们用图描述一下注入关系：

```
┌───────────────┐
│MvcController  │
├───────────────┤   ┌────────────────┐
│- userService ─┼──▶│UserServiceProxy│
└───────────────┘   ├────────────────┤
                    │- jdbcTemplate  │
                    ├────────────────┤   ┌────────────────┐
                    │- target       ─┼──▶│UserService     │
                    └────────────────┘   ├────────────────┤   ┌────────────┐
                                         │- jdbcTemplate ─┼──▶│JdbcTemplate│
                                         └────────────────┘   └────────────┘
```

注意到上图的`UserService`已经脱离了IoC容器的管理，因为此时`UserService`对应的`BeanDefinition`中，存放的instance是`UserServiceProxy`。

可见，引入`BeanPostProcessor`可以实现Proxy机制，但也让依赖注入变得更加复杂。

但是我们仔细分析依赖关系，还是可以总结出两条原则：

1. 一个Bean如果被Proxy替换，则依赖它的Bean应注入Proxy，即上图的`MvcController`应注入`UserServiceProxy`；
2. 一个Bean如果被Proxy替换，如果要注入依赖，则应该注入到原始对象，即上图的`JdbcTemplate`应注入到原始的`UserService`。

基于这个原则，要满足条件1是很容易的，因为只要创建Bean完成后，立刻调用`BeanPostProcessor`就实现了替换，后续其他Bean引用的肯定就是Proxy了。
先改造创建Bean的流程，在创建`@Configuration`后，接着创建`BeanPostProcessor`，再创建其他普通Bean：

```java
public AnnotationConfigApplicationContext(Class<?> configClass, PropertyResolver propertyResolver) {
    ...
    // 创建@Configuration类型的Bean
    this.beans.values().stream()
            // 过滤出@Configuration
            .filter(this::isConfigurationDefinition).sorted().map(def -> {
                // 创建Bean实例
                createBeanAsEarlySingleton(def);
                return def.getName();
            }).collect(Collectors.toList());

    // 创建BeanPostProcessor类型的Bean
    List<BeanPostProcessor> processors = this.beans.values().stream()
            // 过滤出BeanPostProcessor
            .filter(this::isBeanPostProcessorDefinition)
            // 排序
            .sorted()
            // instantiate and collect
            .map(def -> {
                return (BeanPostProcessor) createBeanAsEarlySingleton(def);
            }).collect(Collectors.toList());
    this.beanPostProcessors.addAll(processors);

    // 创建其他普通Bean...
    ...
}
```

再继续修改`createBeanAsEarlySingleton()`，创建Bean实例后，调用`BeanPostProcessor`处理：

```java
public Object createBeanAsEarlySingleton(BeanDefinition def) {
    ...

    // 创建Bean实例:
    Object instance = ...;
    def.setInstance(instance);

    // 调用BeanPostProcessor处理Bean
    for (BeanPostProcessor processor : beanPostProcessors) {
        Object processed = processor.postProcessBeforeInitialization(def.getInstance(), def.getName());
        // 如果一个BeanPostProcessor替换了原始Bean，则更新Bean的引用
        if (def.getInstance() != processed) {
            def.setInstance(processed);
        }
    }
    return def.getInstance();
}
```

现在，如果一个Bean被替换为Proxy，那么`BeanDefinition`中的`instance`已经是Proxy了，这时，对这个Bean进行依赖注入会有问题，因为注入的是Proxy而不是原始Bean，怎么办？

这时我们要思考原始Bean去哪了？原始Bean实际上是被`BeanPostProcessor`给丢了！如果`BeanPostProcessor`能保存原始Bean，那么，注入前先找到原始Bean，就可以把依赖正确地注入给原始Bean。
我们给`BeanPostProcessor`加一个`postProcessOnSetProperty()`方法，让它返回原始Bean：

```java
public interface BeanPostProcessor {
    // 注入依赖时,应该使用的Bean实例
    default Object postProcessOnSetProperty(Object bean, String beanName) {
        return bean;
    }
}
```

再继续把`injectBean()`改一下，不要直接拿`BeanDefinition.getInstance()`，而是拿到原始Bean：

```java
void injectBean(BeanDefinition def) {
    // 获取Bean实例，或被代理的原始实例
    Object beanInstance = getProxiedInstance(def);
    try {
        injectProperties(def, def.getBeanClass(), beanInstance);
    } catch (ReflectiveOperationException e) {
        throw new BeanCreationException(e);
    }
}
```

`getProxiedInstance()`就是为了获取原始Bean：

```java
Object getProxiedInstance(BeanDefinition def) {
    Object beanInstance = def.getInstance();
    // 如果Proxy改变了原始Bean，又希望注入到原始Bean，则由BeanPostProcessor指定原始Bean:
    List<BeanPostProcessor> reversedBeanPostProcessors = new ArrayList<>(this.beanPostProcessors);
    Collections.reverse(reversedBeanPostProcessors);
    for (BeanPostProcessor beanPostProcessor : reversedBeanPostProcessors) {
        Object restoredInstance = beanPostProcessor.postProcessOnSetProperty(beanInstance, def.getName());
        if (restoredInstance != beanInstance) {
            beanInstance = restoredInstance;
        }
    }
    return beanInstance;
}
```

这里我们还能处理多次代理的情况，即一个原始Bean，比如`UserService`，被一个事务处理的`BeanPostProcsssor`代理为`UserServiceTx`，又被一个性能监控的`BeanPostProcessor`代理为`UserServiceMetric`，还原的时候，对`BeanPostProcsssor`做一个倒序，先还原为`UserServiceTx`，再还原为`UserService`。

### 测试

我们可以写一个测试来验证Bean的注入是否正确。先定义原始Bean：

```java
import com.zhengqing.spring.annotation.Component;
import com.zhengqing.spring.annotation.Value;

@Component
public class OriginBean {
    @Value("jdbc.username")
    public String username;


    public String password;

    @Value("jdbc.password")
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
```

通过`FirstProxyBeanPostProcessor`代理为`FirstProxyBean`：

```java
import com.zhengqing.spring.annotation.Component;
import com.zhengqing.spring.annotation.Order;
import com.zhengqing.spring.context.BeanPostProcessor;

import java.util.HashMap;
import java.util.Map;

@Order(100)
@Component
public class FirstProxyBeanPostProcessor implements BeanPostProcessor {
    // 保存原始Bean
    Map<String, Object> originBeans = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (OriginBean.class.isAssignableFrom(bean.getClass())) {
            // 检测到OriginBean,创建FirstProxyBean
            FirstProxyBean proxy = new FirstProxyBean((OriginBean) bean);
            // 保存原始Bean
            originBeans.put(beanName, bean);
            // 返回Proxy
            return proxy;
        }
        return bean;
    }

    @Override
    public Object postProcessOnSetProperty(Object bean, String beanName) {
        Object origin = originBeans.get(beanName);
        if (origin != null) {
            // 存在原始Bean时,返回原始Bean
            return origin;
        }
        return bean;
    }
}
```

```java
public class FirstProxyBean extends OriginBean {

    final OriginBean target;

    public FirstProxyBean(OriginBean target) {
        this.target = target;
    }

    @Override
    public void setPassword(String password) {
        target.setPassword(password);
    }

    @Override
    public String getUsername() {
        return target.getUsername();
    }

    @Override
    public String getPassword() {
        return target.getPassword();
    }
}
```

通过`SecondProxyBeanPostProcessor`代理为`SecondProxyBean`：

```java
import com.zhengqing.spring.annotation.Component;
import com.zhengqing.spring.annotation.Order;
import com.zhengqing.spring.context.BeanPostProcessor;

import java.util.HashMap;
import java.util.Map;

@Order(200)
@Component
public class SecondProxyBeanPostProcessor implements BeanPostProcessor {
    // 保存原始Bean
    Map<String, Object> originBeans = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (OriginBean.class.isAssignableFrom(bean.getClass())) {
            // 检测到OriginBean,创建SecondProxyBean
            SecondProxyBean proxy = new SecondProxyBean((OriginBean) bean);
            // 保存原始Bean
            originBeans.put(beanName, bean);
            // 返回Proxy
            return proxy;
        }
        return bean;
    }

    @Override
    public Object postProcessOnSetProperty(Object bean, String beanName) {
        Object origin = originBeans.get(beanName);
        if (origin != null) {
            // 存在原始Bean时,返回原始Bean
            return origin;
        }
        return bean;
    }
}
```

```java
// 代理Bean
public class SecondProxyBean extends OriginBean {
    final OriginBean target;

    public SecondProxyBean(OriginBean target) {
        this.target = target;
    }

    @Override
    public void setPassword(String password) {
        target.setPassword(password);
    }

    @Override
    public String getUsername() {
        return target.getUsername();
    }

    @Override
    public String getPassword() {
        return target.getPassword();
    }
}
```

定义一个Bean，用于检测是否注入了Proxy：

```java
import com.zhengqing.spring.annotation.Autowired;
import com.zhengqing.spring.annotation.Component;

@Component
public class InjectProxyOnConstructorBean {
    public final OriginBean injected;

    public InjectProxyOnConstructorBean(@Autowired OriginBean injected) {
        this.injected = injected;
    }
}
```

测试代码如下：

```java
import scan.test_BeanPostProcessor.com.zhengqing.spring.context.ScanApplication;
import proxy.scan.test_BeanPostProcessor.com.zhengqing.spring.context.InjectProxyOnConstructorBean;
import proxy.scan.test_BeanPostProcessor.com.zhengqing.spring.context.OriginBean;
import proxy.scan.test_BeanPostProcessor.com.zhengqing.spring.context.SecondProxyBean;
import com.zhengqing.spring.context.AnnotationConfigApplicationContext;
import com.zhengqing.spring.io.PropertyResolver;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

public class _test_BeanPostProcessor {

    @Test
    public void test() throws Exception {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(ScanApplication.class, createPropertyResolver());

        // 获取OriginBean的实例,此处获取的应该是SecondProxyBean
        OriginBean proxy = ctx.getBean(OriginBean.class);
        assertSame(SecondProxyBean.class, proxy.getClass());

        // proxy的字段并没有被注入
        assertNull(proxy.username);
        assertNull(proxy.password);

        // 但是调用proxy的getUsername()会最终调用原始Bean的getUsername(),从而返回正确的值
        assertEquals("root", proxy.getUsername());

        // 获取InjectProxyOnConstructorBean实例
        InjectProxyOnConstructorBean inject = ctx.getBean(InjectProxyOnConstructorBean.class);
        // 注入的OriginBean应该为Proxy，而且和前面返回的proxy是同一实例
        assertSame(proxy, inject.injected);
    }

    PropertyResolver createPropertyResolver() {
        Properties ps = new Properties();
        ps.put("jdbc.username", "root");
        ps.put("jdbc.password", "123456");
        PropertyResolver pr = new PropertyResolver(ps);
        return pr;
    }

}
```

从上面的测试代码我们也能看出，对于使用Proxy模式的Bean来说，正常的方法调用对用户是透明的，但是，直接访问Bean注入的字段，如果获取的是Proxy，则字段全部为`null`，因为注入并没有发生在Proxy，而是原始Bean。
这也是为什么当我们需要访问某个注入的Bean时，总是调用方法而不是直接访问字段。

