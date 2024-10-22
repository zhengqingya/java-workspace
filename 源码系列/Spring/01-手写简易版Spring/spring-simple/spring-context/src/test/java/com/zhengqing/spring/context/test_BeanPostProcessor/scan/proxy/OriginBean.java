package com.zhengqing.spring.context.test_BeanPostProcessor.scan.proxy;

import com.zhengqing.spring.annotation.Component;
import com.zhengqing.spring.annotation.Value;

@Component
public class OriginBean {
    @Value("jdbc.username")
    public String username;


    public String password;

    @Value("jdbc.password")
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}


