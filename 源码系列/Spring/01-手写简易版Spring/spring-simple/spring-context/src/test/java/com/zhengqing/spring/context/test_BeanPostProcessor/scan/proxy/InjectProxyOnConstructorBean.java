package com.zhengqing.spring.context.test_BeanPostProcessor.scan.proxy;

import com.zhengqing.spring.annotation.Autowired;
import com.zhengqing.spring.annotation.Component;

@Component
public class InjectProxyOnConstructorBean {
    public final OriginBean injected;

    public InjectProxyOnConstructorBean(@Autowired OriginBean injected) {
        this.injected = injected;
    }
}