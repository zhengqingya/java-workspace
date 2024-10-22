# 创建BeanDefinition

BeanDefinition描述了一个 Bean 的配置信息，包括 Bean 的类型、属性、依赖关系以及其他配置细节。

---

###### BeanDefinition 的作用

1. 定义 Bean 的元数据
    - BeanDefinition 描述了 Bean 的各种属性和配置信息。
    - 包括 Bean 的类型、构造函数参数、属性值、依赖注入关系等。
2. 创建 Bean 实例
    - Spring 容器根据 BeanDefinition 创建 Bean 实例。
    - 容器使用 BeanDefinition 中的信息来初始化 Bean，并管理其生命周期。
3. 配置依赖注入
    - BeanDefinition 中定义了 Bean 的依赖关系。
    - Spring 容器根据这些依赖关系自动注入其他 Bean。
4. 管理 Bean 的生命周期
    - BeanDefinition 可以定义 Bean 的初始化和销毁方法。
    - Spring 容器会在适当的时候调用这些方法。

###### BeanDefinition 的主要属性

BeanDefinition 类包含了许多重要的属性，以下是一些常见的属性：

- Class：Bean 的类型。
- Properties：Bean 的属性及其值。
- Constructor Arguments：构造函数参数。
- Depends On：依赖于其他 Bean。
- Init Method：初始化方法。
- Destroy Method：销毁方法。

---

现在，我们可以用`ResourceResolver`扫描Class，用`PropertyResolver`获取配置，下面，我们开始实现IoC容器。

在IoC容器中，每个Bean都有一个唯一的名字标识。Spring还允许为一个Bean定义多个名字，这里我们简化一下，一个Bean只允许一个名字，因此，很容易想到用一个`Map<String, Object>`保存所有的Bean：

```java
public class AnnotationConfigApplicationContext {
    Map<String, Object> beans;
}
```

这么做不是不可以，但是丢失了大量Bean的定义信息，不便于我们创建Bean以及解析依赖关系。
合理的方式是先定义`BeanDefinition`，它能从Annotation中提取到足够的信息，便于后续创建Bean、设置依赖、调用初始化方法等：

```java
public class BeanDefinition {
    // 全局唯一的Bean Name
    String name;

    // Bean的声明类型
    Class<?> beanClass;

    // Bean的实例
    Object instance = null;

    // 构造方法/null
    Constructor<?> constructor;

    // 工厂方法名称/null
    String factoryName;

    // 工厂方法/null
    Method factoryMethod;

    // Bean的顺序
    int order;

    // 是否标识@Primary
    boolean primary;

    // init/destroy方法名称
    String initMethodName;
    String destroyMethodName;

    // init/destroy方法
    Method initMethod;
    Method destroyMethod;
}
```

对于自己定义的带`@Component`注解的Bean，我们需要获取Class类型，获取构造方法来创建Bean，然后收集`@PostConstruct`和`@PreDestroy`标注的初始化与销毁的方法，以及其他信息，如`@Order`定义Bean的内部排序顺序，`@Primary`定义存在多个相同类型时返回哪个“主要”Bean。

```java

@Component
public class Hello {
    @PostConstruct
    void init() {
    }

    @PreDestroy
    void destroy() {
    }
}
```

对于`@Configuration`定义的`@Bean`方法，我们把它看作Bean的工厂方法，我们需要获取方法返回值作为Class类型，方法本身作为创建Bean的`factoryMethod`，然后收集`@Bean`定义的`initMethod`和`destroyMethod`标识的初始化与销毁的方法名，以及其他`@Order`、`@Primary`等信息。

```java

@Configuration
public class AppConfig {
    @Bean(initMethod = "init", destroyMethod = "close")
    DataSource createDataSource() {
        return new HikariDataSource(...);
    }
}
```

### Bean的声明类型

- 对于`@Component`定义的Bean，声明类型就是其Class本身。
- `@Bean`工厂方法创建的Bean，声明类型与实际类型不一定是同一类型。
    - 上述`createDataSource()`定义的Bean，声明类型是`DataSource`，实际类型却是某个子类，例如`HikariDataSource`。
    - 因此要特别注意，我们在`BeanDefinition`中，存储的`beanClass`是**声明类型**，实际类型不必存储，因为可以通过`instance.getClass()`获得。

```java
public class BeanDefinition {
    // Bean的声明类型:
    Class<?> beanClass;

    // Bean的实例:
    Object instance = null;
}
```

这也引出了下一个问题：如果我们按照名字查找Bean或BeanDefinition，要么拿到唯一实例，要么不存在，即通过查询`Map<String, BeanDefinition>`即可完成：

```java
public class AnnotationConfigApplicationContext {
    Map<String, BeanDefinition> beans;

    // 根据Name查找BeanDefinition，如果Name不存在，返回null
    @Nullable
    public BeanDefinition findBeanDefinition(String name) {
        return this.beans.get(name);
    }
}
```

但是通过类型查找Bean或BeanDefinition，我们没法定义一个`Map<Class, BeanDefinition>`，原因就是Bean的声明类型与实际类型不一定相符，举个例子：

```java

@Configuration
public class AppConfig {
    @Bean
    AtomicInteger counter() {
        return new AtomicInteger();
    }

    @Bean
    Number bigInt() {
        return new BigInteger("1000000000");
    }
}
```

- 调用`getBean(AtomicInteger.class)`时，会获得`counter()`方法创建的唯一实例
- 调用`getBean(Number.class)`时，`counter()`方法和`bigInt()`方法创建的实例均符合要求
- 此时，如果有且仅有一个标注了`@Primary`，就返回标注了`@Primary`的Bean，否则，直接报`NoUniqueBeanDefinitionException`错误。

因此，对于`getBean(Class)`方法，必须遍历找出所有符合类型的Bean，如果不唯一，再判断`@Primary`，才能返回唯一Bean或报错。

我们编写一个找出所有类型的`findBeanDefinitions(Class)`方法如下：

```java
// 根据Type查找若干个BeanDefinition，返回0个或多个
List<BeanDefinition> findBeanDefinitions(Class<?> type) {
    return this.beans.values().stream()
            // 按类型过滤
            .filter(def -> type.isAssignableFrom(def.getBeanClass()))
            // 排序
            .sorted().collect(Collectors.toList());
}
}
```

我们再编写一个`findBeanDefinition(Class)`方法如下：

```java
// 根据Type查找某个BeanDefinition，如果不存在返回null，如果存在多个返回@Primary标注的一个:
@Nullable
public BeanDefinition findBeanDefinition(Class<?> type) {
    List<BeanDefinition> defs = findBeanDefinitions(type);
    if (defs.isEmpty()) { // 没有找到任何BeanDefinition
        return null;
    }
    if (defs.size() == 1) { // 找到唯一一个
        return defs.get(0);
    }
    // 多于一个时，查找@Primary
    List<BeanDefinition> primaryDefs = defs.stream().filter(def -> def.isPrimary()).collect(Collectors.toList());
    if (primaryDefs.size() == 1) { // @Primary唯一
        return primaryDefs.get(0);
    }
    if (primaryDefs.isEmpty()) { // 不存在@Primary
        throw new NoUniqueBeanDefinitionException(String.format("Multiple bean with type '%s' found, but no @Primary specified.", type.getName()));
    } else { // @Primary不唯一
        throw new NoUniqueBeanDefinitionException(String.format("Multiple bean with type '%s' found, and multiple @Primary specified.", type.getName()));
    }
}
```

现在，我们已经定义好了数据结构，下面开始获取所有`BeanDefinition`信息，实际分两步：

```java
public class AnnotationConfigApplicationContext {
    Map<String, BeanDefinition> beans;

    public AnnotationConfigApplicationContext(Class<?> configClass, PropertyResolver propertyResolver) {
        // 扫描获取所有Bean的Class类型
        Set<String> beanClassNames = scanForClassNames(configClass);

        // 创建Bean的定义
        this.beans = createBeanDefinitions(beanClassNames);
    }
    ...
}
```

第一步:

1. 扫描指定包下的所有Class
2. 通过`@Import`导入的Class
3. 返回Class全限定名

```java
protected Set<String> scanForClassNames(Class<?> configClass) {
    // 获取@ComponentScan注解
    ComponentScan scan = AnnotationUtil.getAnnotation(configClass, ComponentScan.class);
    // 获取注解配置的package名字,未配置则默认当前类所在包
    final String[] scanPackages = scan == null || scan.value().length == 0 ? new String[]{configClass.getPackage().getName()} : scan.value();
    log.debug("component scan in packages: {}", Arrays.toString(scanPackages));

    Set<String> classNameSet = new HashSet<>();
    // 扫描所有包
    for (String pkg : scanPackages) {
        log.debug("scan package: {}", pkg);
        ResourceResolver rr = new ResourceResolver(pkg);
        List<String> classList = rr.scan(Resource::getPath);
        if (log.isDebugEnabled()) {
            classList.forEach((className) -> log.debug("class found by component scan: {}", className));
        }
        classNameSet.addAll(classList);
    }

    // 查找@Import(RedisConfig.class)
    Import importConfig = configClass.getAnnotation(Import.class);
    if (importConfig != null) {
        for (Class<?> importConfigClass : importConfig.value()) {
            String importClassName = importConfigClass.getName();
            if (classNameSet.contains(importClassName)) {
                log.warn("ignore import: " + importClassName + " for it is already been scanned.");
            } else {
                log.debug("class found by import: {}", importClassName);
                classNameSet.add(importClassName);
            }
        }
    }
    return classNameSet;
}
```

第二步：根据扫描的ClassName创建BeanDefinition

```java
Map<String, BeanDefinition> createBeanDefinitions(Set<String> classNameSet) {
    Map<String, BeanDefinition> defs = new HashMap<>();
    for (String className : classNameSet) {
        // 获取Class
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new BeanCreationException(e);
        }
        if (clazz.isAnnotation() || clazz.isEnum() || clazz.isInterface()) {
            continue;
        }
        // 是否标注@Component?
        Component component = AnnotationUtil.getAnnotation(clazz, Component.class);
        if (component != null) {
            log.debug("found component: {}", clazz.getName());
            int mod = clazz.getModifiers();
            if (Modifier.isAbstract(mod)) {
                throw new BeanDefinitionException("@Component class " + clazz.getName() + " must not be abstract.");
            }
            if (Modifier.isPrivate(mod)) {
                throw new BeanDefinitionException("@Component class " + clazz.getName() + " must not be private.");
            }

            String beanName = ClassUtil.getClassName(clazz, false);
            BeanDefinition def = new BeanDefinition(beanName, clazz,
                    getSuitableConstructor(clazz),
                    getOrder(clazz),
                    clazz.isAnnotationPresent(Primary.class),
                    // init/destroy方法名称
                    null, null,
                    // @PostConstruct/@PreDestroy 方法
                    ClassUtils.findAnnotationMethod(clazz, PostConstruct.class),
                    ClassUtils.findAnnotationMethod(clazz, PreDestroy.class));
            addBeanDefinitions(defs, def);
            log.debug("define bean: {}", def);

            Configuration configuration = AnnotationUtil.getAnnotation(clazz, Configuration.class);
            if (configuration != null) {
                // 扫描@Bean方法
                scanFactoryMethods(beanName, clazz, defs);
            }
        }
    }
    return defs;
}
```

上述代码需要注意的一点是，查找`@Component`时，并不是简单地在Class定义查看`@Component`注解，因为Spring的`@Component`是可以扩展的，例如，标记为`Controller`的Class也符合要求：

```java

@Controller
public class MvcController {...
}
```

原因就在于，`@Controller`注解的定义包含了`@Component`：

```java

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Controller {
    String value() default "";
}
```

所以，判断是否存在`@Component`，不但要在当前类查找`@Component`，还要在当前类的所有注解上，查找该注解是否有`@Component`，因此，我们编写了一个能递归查找注解的方法：

```java
public class ClassUtils {
    public static <A extends Annotation> A findAnnotation(Class<?> target, Class<A> annoClass) {
        A a = target.getAnnotation(annoClass);
        for (Annotation anno : target.getAnnotations()) {
            Class<? extends Annotation> annoType = anno.annotationType();
            if (!ClassUtil.getPackage(annoType).equals("java.lang.annotation")) {
                A found = findAnnotation(annoType, annoClass);
                if (found != null) {
                    if (a != null) {
                        throw new BeanDefinitionException("Duplicate @" + annoClass.getSimpleName() + " found on class " + target.getSimpleName());
                    }
                    a = found;
                }
            }
        }
        return a;
    }
}
```

带有`@Configuration`注解的Class，视为Bean的工厂，我们需要继续在`scanFactoryMethods()`中查找`@Bean`标注的方法：

```java
void scanFactoryMethods(String factoryBeanName, Class<?> clazz, Map<String, BeanDefinition> defs) {
    for (Method method : clazz.getDeclaredMethods()) {
        // 是否带有@Bean标注
        Bean bean = method.getAnnotation(Bean.class);
        if (bean != null) {
            // Bean的声明类型是方法返回类型
            Class<?> beanClass = method.getReturnType();
            var def = new BeanDefinition(
                    ClassUtils.getBeanName(method), beanClass,
                    factoryBeanName,
                    // 创建Bean的工厂方法
                    method,
                    // @Order
                    getOrder(method),
                    // 是否存在@Primary标注?
                    method.isAnnotationPresent(Primary.class),
                    // init方法名称
                    bean.initMethod().isEmpty() ? null : bean.initMethod(),
                    // destroy方法名称
                    bean.destroyMethod().isEmpty() ? null : bean.destroyMethod(),
                    // @PostConstruct / @PreDestroy方法
                    null, null);
            addBeanDefinitions(defs, def);
        }
    }
}
```

注意到`@Configuration`注解本身又用`@Component`注解修饰了，因此，对于一个`@Configuration`来说：

```java

@Configuration
public class DateTimeConfig {
    @Bean
    LocalDateTime local() {
        return LocalDateTime.now();
    }

    @Bean
    ZonedDateTime zoned() {
        return ZonedDateTime.now();
    }
}
```

实际上创建了3个`BeanDefinition`：

- DateTimeConfig本身
- LocalDateTime
- ZonedDateTime

不创建`DateTimeConfig`行不行？不行，因为后续没有`DateTimeConfig`的实例，无法调用`local()`和`zoned()`方法。
因为当前我们只创建了`BeanDefinition`，所以对于`LocalDateTime`和`ZonedDateTime`的`BeanDefinition`来说，还必须保存`DateTimeConfig`的名字，将来才能通过名字查找`DateTimeConfig`的实例。

有的同学注意到我们同时存储了`initMethodName`和`initMethod`，以及`destroyMethodName`和`destroyMethod`，这是因为在`@Component`声明的Bean中，我们可以根据`@PostConstruct`和`@PreDestroy`直接拿到Method本身，而在`@Bean`声明的Bean中，我们拿不到Method，只能从`@Bean`
注解提取出字符串格式的方法名称，因此，存储在`BeanDefinition`的方法名称与方法，其中至少有一个为`null`。

现在，我们已经能扫描并创建所有的`BeanDefinition`，只是目前每个`BeanDefinition`内部的`instance`还是`null`，因为我们后续才会创建真正的Bean。

---

本文完整的 `AnnotationConfigApplicationContext.java`

```java
import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import com.zhengqing.spring.annotation.*;
import com.zhengqing.spring.exception.BeanCreationException;
import com.zhengqing.spring.exception.BeanDefinitionException;
import com.zhengqing.spring.exception.NoUniqueBeanDefinitionException;
import com.zhengqing.spring.io.PropertyResolver;
import com.zhengqing.spring.io.Resource;
import com.zhengqing.spring.io.ResourceResolver;
import com.zhengqing.spring.util.ClassUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class AnnotationConfigApplicationContext {
    protected final PropertyResolver propertyResolver;
    protected final Map<String, BeanDefinition> beans;

    public AnnotationConfigApplicationContext(Class<?> configClass, PropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;

        // 扫描获取所有Bean的Class类型
        final Set<String> beanClassNames = scanForClassNames(configClass);

        // 创建Bean的定义
        this.beans = createBeanDefinitions(beanClassNames);
    }

    /**
     * 根据Name查找BeanDefinition，如果Name不存在，返回null
     */
    @Nullable
    public BeanDefinition findBeanDefinition(String name) {
        return this.beans.get(name);
    }

    /**
     * 根据Type查找若干个BeanDefinition，返回0个或多个。
     */
    public List<BeanDefinition> findBeanDefinitions(Class<?> type) {
        return this.beans.values().stream()
                // 按类型过滤
                .filter(def -> type.isAssignableFrom(def.getBeanClass()))
                // 排序
                .sorted().collect(Collectors.toList());
    }

    /**
     * 根据Type查找某个BeanDefinition，如果不存在返回null，如果存在多个返回@Primary标注的一个，如果有多个@Primary标注，或没有@Primary标注但找到多个，均抛出NoUniqueBeanDefinitionException
     */
    @Nullable
    public BeanDefinition findBeanDefinition(Class<?> type) {
        List<BeanDefinition> defs = findBeanDefinitions(type);
        if (defs.isEmpty()) {
            return null;
        }
        if (defs.size() == 1) {
            return defs.get(0);
        }
        // 多于一个时，查找@Primary
        List<BeanDefinition> primaryDefs = defs.stream().filter(def -> def.isPrimary()).collect(Collectors.toList());
        if (primaryDefs.size() == 1) {
            return primaryDefs.get(0);
        }
        if (primaryDefs.isEmpty()) {
            throw new NoUniqueBeanDefinitionException(String.format("Multiple bean with type '%s' found, but no @Primary specified.", type.getName()));
        } else {
            throw new NoUniqueBeanDefinitionException(String.format("Multiple bean with type '%s' found, and multiple @Primary specified.", type.getName()));
        }
    }

    /**
     * 扫描指定包下的所有Class，然后返回Class全限定名
     */
    protected Set<String> scanForClassNames(Class<?> configClass) {
        // 获取@ComponentScan注解
        ComponentScan scan = AnnotationUtil.getAnnotation(configClass, ComponentScan.class);
        // 获取注解配置的package名字,未配置则默认当前类所在包
        final String[] scanPackages = scan == null || scan.value().length == 0 ? new String[]{configClass.getPackage().getName()} : scan.value();
        log.debug("component scan in packages: {}", Arrays.toString(scanPackages));

        Set<String> classNameSet = new HashSet<>();
        // 扫描所有包
        for (String pkg : scanPackages) {
            log.debug("scan package: {}", pkg);
            ResourceResolver rr = new ResourceResolver(pkg);
            List<String> classList = rr.scan(Resource::getPath);
            if (log.isDebugEnabled()) {
                classList.forEach((className) -> log.debug("class found by component scan: {}", className));
            }
            classNameSet.addAll(classList);
        }

        // 查找@Import(RedisConfig.class)
        Import importConfig = configClass.getAnnotation(Import.class);
        if (importConfig != null) {
            for (Class<?> importConfigClass : importConfig.value()) {
                String importClassName = importConfigClass.getName();
                if (classNameSet.contains(importClassName)) {
                    log.warn("ignore import: " + importClassName + " for it is already been scanned.");
                } else {
                    log.debug("class found by import: {}", importClassName);
                    classNameSet.add(importClassName);
                }
            }
        }
        return classNameSet;
    }

    /**
     * 根据扫描的ClassName创建BeanDefinition
     */
    Map<String, BeanDefinition> createBeanDefinitions(Set<String> classNameSet) {
        Map<String, BeanDefinition> defs = new HashMap<>();
        for (String className : classNameSet) {
            // 获取Class
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new BeanCreationException(e);
            }
            if (clazz.isAnnotation() || clazz.isEnum() || clazz.isInterface()) {
                continue;
            }
            // 是否标注@Component?
            Component component = AnnotationUtil.getAnnotation(clazz, Component.class);
            if (component != null) {
                log.debug("found component: {}", clazz.getName());
                int mod = clazz.getModifiers();
                if (Modifier.isAbstract(mod)) {
                    throw new BeanDefinitionException("@Component class " + clazz.getName() + " must not be abstract.");
                }
                if (Modifier.isPrivate(mod)) {
                    throw new BeanDefinitionException("@Component class " + clazz.getName() + " must not be private.");
                }

                String beanName = ClassUtils.getBeanName(clazz);
                BeanDefinition def = new BeanDefinition(beanName, clazz,
                        getSuitableConstructor(clazz),
                        getOrder(clazz),
                        clazz.isAnnotationPresent(Primary.class),
                        // init/destroy方法名称
                        null, null,
                        // @PostConstruct/@PreDestroy 方法
                        ClassUtils.findAnnotationMethod(clazz, PostConstruct.class),
                        ClassUtils.findAnnotationMethod(clazz, PreDestroy.class));
                addBeanDefinitions(defs, def);
                log.debug("define bean: {}", def);

                Configuration configuration = AnnotationUtil.getAnnotation(clazz, Configuration.class);
                if (configuration != null) {
                    // 扫描@Bean方法
                    scanFactoryMethods(beanName, clazz, defs);
                }
            }
        }
        return defs;
    }

    /**
     * Get public constructor or non-public constructor as fallback.
     */
    Constructor<?> getSuitableConstructor(Class<?> clazz) {
        Constructor<?>[] cons = clazz.getConstructors();
        if (cons.length == 0) {
            cons = clazz.getDeclaredConstructors();
            if (cons.length != 1) {
                throw new BeanDefinitionException("More than one constructor found in class " + clazz.getName() + ".");
            }
        }
        if (cons.length != 1) {
            throw new BeanDefinitionException("More than one public constructor found in class " + clazz.getName() + ".");
        }
        return cons[0];
    }

    int getOrder(Class<?> clazz) {
        Order order = clazz.getAnnotation(Order.class);
        return order == null ? Integer.MAX_VALUE : order.value();
    }

    int getOrder(Method method) {
        Order order = method.getAnnotation(Order.class);
        return order == null ? Integer.MAX_VALUE : order.value();
    }

    /**
     * Check and add bean definitions.
     */
    void addBeanDefinitions(Map<String, BeanDefinition> defs, BeanDefinition def) {
        if (defs.put(def.getName(), def) != null) {
            throw new BeanDefinitionException("Duplicate bean name: " + def.getName());
        }
    }


    /**
     * 扫描@Bean方法
     */
    void scanFactoryMethods(String factoryBeanName, Class<?> clazz, Map<String, BeanDefinition> defs) {
        for (Method method : clazz.getDeclaredMethods()) {
            Bean bean = method.getAnnotation(Bean.class);
            if (bean != null) {
                int mod = method.getModifiers();
                if (Modifier.isAbstract(mod)) {
                    throw new BeanDefinitionException("@Bean method " + clazz.getName() + "." + method.getName() + " must not be abstract.");
                }
                if (Modifier.isFinal(mod)) {
                    throw new BeanDefinitionException("@Bean method " + clazz.getName() + "." + method.getName() + " must not be final.");
                }
                if (Modifier.isPrivate(mod)) {
                    throw new BeanDefinitionException("@Bean method " + clazz.getName() + "." + method.getName() + " must not be private.");
                }
                Class<?> beanClass = method.getReturnType();
                if (beanClass.isPrimitive()) {
                    throw new BeanDefinitionException("@Bean method " + clazz.getName() + "." + method.getName() + " must not return primitive type.");
                }
                if (beanClass == void.class || beanClass == Void.class) {
                    throw new BeanDefinitionException("@Bean method " + clazz.getName() + "." + method.getName() + " must not return void.");
                }
                BeanDefinition def = new BeanDefinition(ClassUtils.getBeanName(method),
                        beanClass,
                        factoryBeanName,
                        method,
                        getOrder(method),
                        method.isAnnotationPresent(Primary.class),
                        bean.initMethod().isEmpty() ? null : bean.initMethod(),
                        bean.destroyMethod().isEmpty() ? null : bean.destroyMethod(),
                        null, null);
                addBeanDefinitions(defs, def);
                log.debug("define bean: {}", def);
            }
        }
    }

}
```

`BeanDefinition.java`

```java
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@Getter
public class BeanDefinition implements Comparable<BeanDefinition> {

    // 全局唯一的Bean Name
    private final String name;
    // Bean的声明类型
    private final Class<?> beanClass;
    // Bean的实例
    private Object instance = null;
    // 构造方法/null
    private final Constructor<?> constructor;
    // 工厂方法名称/null
    private final String factoryName;
    // 工厂方法/null
    private final Method factoryMethod;
    // Bean的顺序
    private final int order;
    // 是否标识@Primary
    private final boolean primary;

    // autowired and called init method:
    private boolean init = false;

    // init/destroy方法名称
    private String initMethodName;
    private String destroyMethodName;

    // init/destroy方法
    private Method initMethod;
    private Method destroyMethod;

    public BeanDefinition(String name, Class<?> beanClass, Constructor<?> constructor,
                          int order, boolean primary, String initMethodName,
                          String destroyMethodName, Method initMethod, Method destroyMethod) {
        this.name = name;
        this.beanClass = beanClass;
        this.constructor = constructor;
        this.factoryName = null;
        this.factoryMethod = null;
        this.order = order;
        this.primary = primary;
        constructor.setAccessible(true);
        setInitAndDestroyMethod(initMethodName, destroyMethodName, initMethod, destroyMethod);
    }

    public BeanDefinition(String name, Class<?> beanClass, String factoryName, Method factoryMethod,
                          int order, boolean primary, String initMethodName,
                          String destroyMethodName, Method initMethod, Method destroyMethod) {
        this.name = name;
        this.beanClass = beanClass;
        this.constructor = null;
        this.factoryName = factoryName;
        this.factoryMethod = factoryMethod;
        this.order = order;
        this.primary = primary;
        factoryMethod.setAccessible(true);
        setInitAndDestroyMethod(initMethodName, destroyMethodName, initMethod, destroyMethod);
    }

    private void setInitAndDestroyMethod(String initMethodName, String destroyMethodName, Method initMethod, Method destroyMethod) {
        this.initMethodName = initMethodName;
        this.destroyMethodName = destroyMethodName;
        if (initMethod != null) {
            initMethod.setAccessible(true);
        }
        if (destroyMethod != null) {
            destroyMethod.setAccessible(true);
        }
        this.initMethod = initMethod;
        this.destroyMethod = destroyMethod;
    }

    @Override
    public int compareTo(BeanDefinition def) {
        int cmp = Integer.compare(this.order, def.order);
        if (cmp != 0) {
            return cmp;
        }
        return this.name.compareTo(def.name);
    }
}
```
