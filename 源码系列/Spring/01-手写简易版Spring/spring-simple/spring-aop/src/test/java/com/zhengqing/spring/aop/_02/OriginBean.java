package com.zhengqing.spring.aop._02;


import com.zhengqing.spring.annotation.Around;
import com.zhengqing.spring.annotation.Component;
import com.zhengqing.spring.annotation.Value;

@Component
@Around("aroundInvocationHandler")
public class OriginBean {
    @Value("jdbc.username")
    public String name;

    @Log
    public String hello() {
        return "Hello ..." + name;
    }

    public String hi() {
        return "Hi ..." + name;
    }
}