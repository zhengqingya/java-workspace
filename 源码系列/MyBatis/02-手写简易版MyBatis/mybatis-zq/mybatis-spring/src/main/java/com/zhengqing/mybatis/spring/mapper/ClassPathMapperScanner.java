package com.zhengqing.mybatis.spring.mapper;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;
import java.util.Set;

/**
 * <p> 自定义扫描器 </p>
 *
 * @author zhengqingya
 * @description 扫描指定包，发现并注册bean
 * @date 2024/5/5 05:46
 */
public class ClassPathMapperScanner extends ClassPathBeanDefinitionScanner {
    public ClassPathMapperScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
//        return super.isCandidateComponent(metadataReader); // 判断是否存在@Component注解
        return true;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
//        return super.isCandidateComponent(beanDefinition); // 判断是否不是接口
        return beanDefinition.getMetadata().isInterface();
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
            AbstractBeanDefinition beanDefinition = (AbstractBeanDefinition) beanDefinitionHolder.getBeanDefinition();
            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanDefinition.getBeanClassName());
            beanDefinition.setBeanClass(MapperFactoryBean.class);
        }
        return beanDefinitionHolders;
    }
}
