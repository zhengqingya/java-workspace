package com.zhengqing.mybatis.spring.annotation;

import com.zhengqing.mybatis.spring.mapper.ClassPathMapperScanner;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * <p> 自定义注册BeanDefinition </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/5/5 05:03
 */
public class MapperScannerRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
//        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition().getBeanDefinition();
//        beanDefinition.setBeanClass(MapperFactoryBean.class);
//        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(UserMapper.class);
//        registry.registerBeanDefinition("userMapper", beanDefinition);

        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(MapperScan.class.getName());
        String packageName = (String) annotationAttributes.get("value");

        ClassPathMapperScanner classPathMapperScanner = new ClassPathMapperScanner(registry);
        classPathMapperScanner.doScan(packageName);
    }
}
