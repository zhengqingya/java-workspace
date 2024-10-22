package com.zhengqing.spring.aop._01;

public class OriginBean {
    public String name;

    @Log
    public String hello() {
        return "Hello ..." + name;
    }

    public String hi() {
        return "Hi ..." + name;
    }
}