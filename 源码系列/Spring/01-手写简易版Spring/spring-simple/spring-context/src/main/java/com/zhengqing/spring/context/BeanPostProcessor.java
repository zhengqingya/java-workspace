package com.zhengqing.spring.context;

public interface BeanPostProcessor {

    /**
     * 在 Bean 的初始化方法（如标注了 @PostConstruct 的方法或通过 <bean init-method="..."> 配置的方法）调用之前被调用。
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * 在 Bean 的初始化方法调用之后被调用。
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * 返回原始Bean
     * Invoked before bean.setXyz() called.
     */
    default Object postProcessOnSetProperty(Object bean, String beanName) {
        return bean;
    }
}
