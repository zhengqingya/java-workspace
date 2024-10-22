package com.zhengqing.spring.context.test_BeanPostProcessor.scan.proxy;

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

