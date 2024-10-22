package com.zhengqing.spring.context;

import com.zhengqing.spring.exception.BeanCreationException;
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Objects;

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

    public void setInstance(Object instance) {
        Objects.requireNonNull(instance, "Bean instance is null.");
        if (!this.beanClass.isAssignableFrom(instance.getClass())) {
            throw new BeanCreationException(String.format("Instance '%s' of Bean '%s' is not the expected type: %s", instance, instance.getClass().getName(), this.beanClass.getName()));
        }
        this.instance = instance;
    }

    public Object getRequiredInstance() {
        if (this.instance == null) {
            throw new BeanCreationException(String.format("Instance of bean with name '%s' and type '%s' is not instantiated during current stage.", this.getName(), this.getBeanClass().getName()));
        }
        return this.instance;
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
