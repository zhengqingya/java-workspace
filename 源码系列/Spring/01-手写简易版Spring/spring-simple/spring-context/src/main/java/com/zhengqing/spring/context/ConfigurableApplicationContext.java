package com.zhengqing.spring.context;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Used for BeanPostProcessor.
 */
public interface ConfigurableApplicationContext extends ApplicationContext {

    List<BeanDefinition> findBeanDefinitions(Class<?> type);

    @Nullable
    BeanDefinition findBeanDefinition(Class<?> type);

    @Nullable
    BeanDefinition findBeanDefinition(String name);

    @Nullable
    BeanDefinition findBeanDefinition(String name, Class<?> requiredType);

    Object createBeanAsEarlySingleton(BeanDefinition def);
}
