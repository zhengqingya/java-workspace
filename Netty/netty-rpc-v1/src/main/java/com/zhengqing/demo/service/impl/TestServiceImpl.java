package com.zhengqing.demo.service.impl;

import com.zhengqing.demo.service.TestService;

public class TestServiceImpl implements TestService {
    @Override
    public String hello(String msg) {
        return "hello: " + msg;
    }

}