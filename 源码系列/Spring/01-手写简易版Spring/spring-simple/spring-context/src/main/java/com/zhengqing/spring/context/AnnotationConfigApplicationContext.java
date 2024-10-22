package com.zhengqing.spring.context;

import cn.hutool.core.annotation.AnnotationUtil;
import com.google.common.collect.Lists;
import com.zhengqing.spring.annotation.*;
import com.zhengqing.spring.exception.*;
import com.zhengqing.spring.io.PropertyResolver;
import com.zhengqing.spring.io.Resource;
import com.zhengqing.spring.io.ResourceResolver;
import com.zhengqing.spring.util.ApplicationContextUtils;
import com.zhengqing.spring.util.ClassUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class AnnotationConfigApplicationContext implements ConfigurableApplicationContext {
    protected final PropertyResolver propertyResolver;
    protected final Map<String, BeanDefinition> beans;

    private Set<String> creatingBeanNames;
    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public AnnotationConfigApplicationContext(Class<?> configClass, PropertyResolver propertyResolver) {
        ApplicationContextUtils.setApplicationContext(this);

        this.propertyResolver = propertyResolver;

        // 扫描获取所有Bean的Class类型
        final Set<String> beanClassNames = scanForClassNames(configClass);

        // 创建Bean的定义
        this.beans = createBeanDefinitions(beanClassNames);

        // 创建BeanName检测循环依赖
        this.creatingBeanNames = new HashSet<>();

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

        // 创建其他普通Bean
        createNormalBeans();

        // 通过字段和set方法注入依赖
        this.beans.values().forEach(def -> {
            injectBean(def);
        });

        // 调用init方法
        this.beans.values().forEach(def -> {
            initBean(def);
        });

        if (log.isDebugEnabled()) {
            this.beans.values().stream().sorted().forEach(def -> {
                log.debug("bean initialized -- bean name: {}", def.getName());
            });
        }
    }

    boolean isBeanPostProcessorDefinition(BeanDefinition def) {
        return BeanPostProcessor.class.isAssignableFrom(def.getBeanClass());
    }

    /**
     * 创建普通的Bean
     */
    void createNormalBeans() {
        List<BeanDefinition> defs = this.beans.values().stream()
                // 过滤出instance==null的BeanDefinition
                .filter(def -> def.getInstance() == null)
                .sorted().collect(Collectors.toList());
        // 依次创建Bean实例
        defs.forEach(def -> {
            // 如果Bean未被创建(可能在其他Bean的构造方法注入前被创建)
            if (def.getInstance() == null) {
                // 创建Bean
                createBeanAsEarlySingleton(def);
            }
        });
    }

    /**
     * 根据Name查找BeanDefinition，如果Name不存在，返回null
     */
    @Override
    @Nullable
    public BeanDefinition findBeanDefinition(String name) {
        return this.beans.get(name);
    }

    /**
     * 根据Type查找若干个BeanDefinition，返回0个或多个。
     */
    @Override
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
    @Override
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
     * 根据Name和Type查找BeanDefinition，如果Name不存在，返回null，如果Name存在，但Type不匹配，抛出异常。
     */
    @Nullable
    @Override
    public BeanDefinition findBeanDefinition(String name, Class<?> requiredType) {
        BeanDefinition def = findBeanDefinition(name);
        if (def == null) {
            return null;
        }
        if (!requiredType.isAssignableFrom(def.getBeanClass())) {
            throw new BeanNotOfRequiredTypeException(String.format("Autowire required type '%s' but bean '%s' has actual type '%s'.", requiredType.getName(), name, def.getBeanClass().getName()));
        }
        return def;
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

    private boolean isConfigurationDefinition(BeanDefinition def) {
        return ClassUtils.findAnnotation(def.getBeanClass(), Configuration.class) != null;
    }

    /**
     * 创建一个Bean，然后使用BeanPostProcessor处理，但不进行字段和方法级别的注入。如果创建的Bean不是Configuration或BeanPostProcessor，则在构造方法中注入的依赖Bean会自动创建。
     */
    @Override
    public Object createBeanAsEarlySingleton(BeanDefinition def) {
        log.debug("Try create bean '{}' as early singleton: {}", def.getName(), def.getBeanClass().getName());
        // 1、检测循环依赖
        if (!this.creatingBeanNames.add(def.getName())) {
            throw new UnsatisfiedDependencyException(String.format("Circular dependency detected when create bean '%s'", def.getName()));
        }

        // 创建方式：构造方法或工厂方法
        Executable createFn = def.getFactoryName() == null ? def.getConstructor() : def.getFactoryMethod();

        // 2、拿到方法参数
        final Parameter[] parameters = createFn.getParameters();
        final Annotation[][] parametersAnnos = createFn.getParameterAnnotations();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            final Parameter param = parameters[i];
            final Annotation[] paramAnnos = parametersAnnos[i];
            final Value value = ClassUtils.getAnnotation(paramAnnos, Value.class);
            final Autowired autowired = ClassUtils.getAnnotation(paramAnnos, Autowired.class);

            // @Configuration类型的Bean是工厂，不允许使用@Autowired创建
            final boolean isConfiguration = isConfigurationDefinition(def);
            if (isConfiguration && autowired != null) {
                throw new BeanCreationException(String.format("Cannot specify @Autowired when create @Configuration bean '%s': %s.", def.getName(), def.getBeanClass().getName()));
            }

            // 参数需要@Value或@Autowired两者之一
            if (value != null && autowired != null) {
                throw new BeanCreationException(String.format("Cannot specify both @Autowired and @Value when create bean '%s': %s.", def.getName(), def.getBeanClass().getName()));
            }
            if (value == null && autowired == null) {
                throw new BeanCreationException(String.format("Must specify @Autowired or @Value when create bean '%s': %s.", def.getName(), def.getBeanClass().getName()));
            }
            // 参数类型
            final Class<?> type = param.getType();
            if (value != null) {
                // 参数是@Value
                args[i] = this.propertyResolver.getRequiredProperty(value.value(), type);
            } else {
                // 参数是@Autowired
                String name = autowired.name();
                boolean required = autowired.value();
                // 依赖的BeanDefinition
                BeanDefinition dependsOnDef = name.isEmpty() ? findBeanDefinition(type) : findBeanDefinition(name, type);
                // 检测required==true
                if (required && dependsOnDef == null) {
                    throw new BeanCreationException(String.format("Missing autowired bean with type '%s' when create bean '%s': %s.", type.getName(), def.getName(), def.getBeanClass().getName()));
                }
                if (dependsOnDef != null) {
                    // 获取依赖Bean
                    Object autowiredBeanInstance = dependsOnDef.getInstance();
                    if (autowiredBeanInstance == null && !isConfiguration) {
                        // 当前依赖Bean尚未初始化，递归调用初始化该依赖Bean
                        autowiredBeanInstance = createBeanAsEarlySingleton(dependsOnDef);
                    }
                    args[i] = autowiredBeanInstance;
                } else {
                    args[i] = null;
                }
            }
        }

        // 3、上面已拿到所有方法参数，开始创建Bean实例
        Object instance = null;
        if (def.getFactoryName() == null) {
            // 用构造方法创建
            try {
                instance = def.getConstructor().newInstance(args);
            } catch (Exception e) {
                throw new BeanCreationException(String.format("Exception when create bean '%s': %s", def.getName(), def.getBeanClass().getName()), e);
            }
        } else {
            // 用@Bean方法创建
            Object configInstance = getBean(def.getFactoryName());
            try {
                instance = def.getFactoryMethod().invoke(configInstance, args);
            } catch (Exception e) {
                throw new BeanCreationException(String.format("Exception when create bean '%s': %s", def.getName(), def.getBeanClass().getName()), e);
            }
        }
        def.setInstance(instance);

        // 4、调用BeanPostProcessor处理Bean
        for (BeanPostProcessor processor : beanPostProcessors) {
            Object processed = processor.postProcessBeforeInitialization(def.getInstance(), def.getName());
            // 如果一个BeanPostProcessor替换了原始Bean，则更新Bean的引用:
            if (def.getInstance() != processed) {
                def.setInstance(processed);
            }
        }
        return def.getInstance();
    }

    /**
     * 通过Name查找Bean，不存在时抛出NoSuchBeanDefinitionException
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(String name) {
        BeanDefinition def = this.beans.get(name);
        if (def == null) {
            throw new NoSuchBeanDefinitionException(String.format("No bean defined with name '%s'.", name));
        }
        return (T) def.getRequiredInstance();
    }


    /**
     * 通过Type查找Bean，不存在抛出NoSuchBeanDefinitionException，存在多个但缺少唯一@Primary标注抛出NoUniqueBeanDefinitionException
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        BeanDefinition def = findBeanDefinition(requiredType);
        if (def == null) {
            throw new NoSuchBeanDefinitionException(String.format("No bean defined with type '%s'.", requiredType));
        }
        return (T) def.getRequiredInstance();
    }

    /**
     * 注入依赖但不调用init方法
     */
    void injectBean(BeanDefinition def) {
        // 获取Bean实例，或被代理的原始实例
        Object beanInstance = getProxiedInstance(def);
        try {
            injectProperties(def, def.getBeanClass(), beanInstance);
        } catch (ReflectiveOperationException e) {
            throw new BeanCreationException(e);
        }
    }

    /**
     * 获取原始Bean
     */
    private Object getProxiedInstance(BeanDefinition def) {
        Object beanInstance = def.getInstance();
        // 如果Proxy改变了原始Bean，又希望注入到原始Bean，则由BeanPostProcessor指定原始Bean
        List<BeanPostProcessor> reversedBeanPostProcessors = new ArrayList<>(this.beanPostProcessors);
        Collections.reverse(reversedBeanPostProcessors);
        for (BeanPostProcessor beanPostProcessor : reversedBeanPostProcessors) {
            Object restoredInstance = beanPostProcessor.postProcessOnSetProperty(beanInstance, def.getName());
            if (restoredInstance != beanInstance) {
                log.debug("BeanPostProcessor {} specified injection from {} to {}.", beanPostProcessor.getClass().getSimpleName(), beanInstance.getClass().getSimpleName(), restoredInstance.getClass().getSimpleName());
                beanInstance = restoredInstance;
            }
        }
        return beanInstance;
    }

    /**
     * 注入属性
     */
    void injectProperties(BeanDefinition def, Class<?> clazz, Object bean) throws ReflectiveOperationException {
        // 在当前类查找Field和Method并注入
        for (Field f : clazz.getDeclaredFields()) {
            tryInjectProperties(def, clazz, bean, f);
        }
        for (Method m : clazz.getDeclaredMethods()) {
            tryInjectProperties(def, clazz, bean, m);
        }
        // 在父类查找Field和Method并注入
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            injectProperties(def, superClazz, bean);
        }
    }

    /**
     * 注入单个属性
     */
    void tryInjectProperties(BeanDefinition def, Class<?> clazz, Object bean, AccessibleObject acc) throws ReflectiveOperationException {
        Value value = acc.getAnnotation(Value.class);
        Autowired autowired = acc.getAnnotation(Autowired.class);
        if (value == null && autowired == null) {
            return;
        }

        Field field = null;
        Method method = null;
        if (acc instanceof Field) {
            Field f = (Field) acc;
            checkFieldOrMethod(f);
            acc.setAccessible(true);
            field = f;
        }
        if (acc instanceof Method) {
            Method m = (Method) acc;
            checkFieldOrMethod(m);
            if (m.getParameters().length != 1) {
                throw new BeanDefinitionException(String.format("Cannot inject a non-setter method %s for bean '%s': %s", m.getName(), def.getName(), def.getBeanClass().getName()));
            }
            m.setAccessible(true);
            method = m;
        }

        String accessibleName = field != null ? field.getName() : method.getName();
        Class<?> accessibleType = field != null ? field.getType() : method.getParameterTypes()[0];

        if (value != null && autowired != null) {
            throw new BeanCreationException(String.format("Cannot specify both @Autowired and @Value when inject %s.%s for bean '%s': %s", clazz.getSimpleName(), accessibleName, def.getName(), def.getBeanClass().getName()));
        }

        // @Value注入
        if (value != null) {
            Object propValue = this.propertyResolver.getRequiredProperty(value.value(), accessibleType);
            if (field != null) {
                log.debug("Field injection: {}.{} = {}", def.getBeanClass().getName(), accessibleName, propValue);
                field.set(bean, propValue);
            }
            if (method != null) {
                log.debug("Method injection: {}.{} ({})", def.getBeanClass().getName(), accessibleName, propValue);
                method.invoke(bean, propValue);
            }
        }

        // @Autowired注入
        if (autowired != null) {
            String name = autowired.name();
            boolean required = autowired.value();
            Object depends = name.isEmpty() ? findBean(accessibleType) : findBean(name, accessibleType);
            if (required && depends == null) {
                throw new UnsatisfiedDependencyException(String.format("Dependency bean not found when inject %s.%s for bean '%s': %s", clazz.getSimpleName(), accessibleName, def.getName(), def.getBeanClass().getName()));
            }
            if (depends != null) {
                if (field != null) {
                    log.debug("Field injection: {}.{} = {}", def.getBeanClass().getName(), accessibleName, depends);
                    field.set(bean, depends);
                }
                if (method != null) {
                    log.debug("Method injection: {}.{} ({})", def.getBeanClass().getName(), accessibleName, depends);
                    method.invoke(bean, depends);
                }
            }
        }
    }

    void checkFieldOrMethod(Member m) {
        int mod = m.getModifiers();
        if (Modifier.isStatic(mod)) {
            throw new BeanDefinitionException("Cannot inject static field: " + m);
        }
        if (Modifier.isFinal(mod)) {
            if (m instanceof Field) {
                Field f = (Field) m;
                throw new BeanDefinitionException("Cannot inject final field: " + f);
            }
            if (m instanceof Method) {
                log.warn("Inject final method should be careful because it is not called on target bean when bean is proxied and may cause NullPointerException.");
            }
        }
    }

    @Nullable
    @SuppressWarnings("unchecked")
    protected <T> T findBean(String name, Class<T> requiredType) {
        BeanDefinition def = findBeanDefinition(name, requiredType);
        if (def == null) {
            return null;
        }
        return (T) def.getRequiredInstance();
    }

    @Nullable
    @SuppressWarnings("unchecked")
    protected <T> T findBean(Class<T> requiredType) {
        BeanDefinition def = findBeanDefinition(requiredType);
        if (def == null) {
            return null;
        }
        return (T) def.getRequiredInstance();
    }

    /**
     * 调用init方法
     */
    void initBean(BeanDefinition def) {
        // 调用init方法
        callMethod(def.getInstance(), def.getInitMethod(), def.getInitMethodName());
    }

    private void callMethod(Object beanInstance, Method method, String namedMethod) {
        // 调用init/destroy方法
        if (method != null) {
            try {
                method.invoke(beanInstance);
            } catch (ReflectiveOperationException e) {
                throw new BeanCreationException(e);
            }
        } else if (namedMethod != null) {
            // 查找initMethod/destroyMethod="xyz"，注意是在实际类型中查找
            Method named = ClassUtils.getNamedMethod(beanInstance.getClass(), namedMethod);
            named.setAccessible(true);
            try {
                named.invoke(beanInstance);
            } catch (ReflectiveOperationException e) {
                throw new BeanCreationException(e);
            }
        }
    }

    @Override
    public boolean containsBean(String name) {
        return this.beans.containsKey(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        T t = findBean(name, requiredType);
        if (t == null) {
            throw new NoSuchBeanDefinitionException(String.format("No bean defined with name '%s' and type '%s'.", name, requiredType));
        }
        return t;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getBeans(Class<T> requiredType) {
        List<BeanDefinition> defs = findBeanDefinitions(requiredType);
        if (defs.isEmpty()) {
            return Lists.newArrayList();
        }
        List<T> list = new ArrayList<>(defs.size());
        for (BeanDefinition def : defs) {
            list.add((T) def.getRequiredInstance());
        }
        return list;
    }

    @Override
    public void close() {
        log.info("Closing {}...", this.getClass().getName());
        this.beans.values().forEach(def -> {
            final Object beanInstance = getProxiedInstance(def);
            callMethod(beanInstance, def.getDestroyMethod(), def.getDestroyMethodName());
        });
        this.beans.clear();
        log.info("{} closed.", this.getClass().getName());
        ApplicationContextUtils.setApplicationContext(null);
    }

}
