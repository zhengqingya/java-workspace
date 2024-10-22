package com.zhengqing.spring.aop._02;


import com.zhengqing.spring.annotation.Bean;
import com.zhengqing.spring.annotation.ComponentScan;
import com.zhengqing.spring.annotation.Configuration;
import com.zhengqing.spring.aop.AroundProxyBeanPostProcessor;

@Configuration
@ComponentScan
public class AroundApplication {

    @Bean
    AroundProxyBeanPostProcessor createAroundProxyBeanPostProcessor() {
        return new AroundProxyBeanPostProcessor();
    }
}
