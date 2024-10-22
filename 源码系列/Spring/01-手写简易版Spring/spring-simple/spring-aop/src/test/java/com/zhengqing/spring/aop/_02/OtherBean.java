package com.zhengqing.spring.aop._02;


import com.zhengqing.spring.annotation.Autowired;
import com.zhengqing.spring.annotation.Component;
import com.zhengqing.spring.annotation.Order;

@Order(0)
@Component
public class OtherBean {

    public OriginBean origin;

    public OtherBean(@Autowired OriginBean origin) {
        this.origin = origin;
    }
}
