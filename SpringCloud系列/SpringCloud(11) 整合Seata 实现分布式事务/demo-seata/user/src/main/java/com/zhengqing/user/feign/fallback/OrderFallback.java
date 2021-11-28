package com.zhengqing.user.feign.fallback;

import com.zhengqing.user.feign.OrderClient;
import org.springframework.stereotype.Component;

@Component
public class OrderFallback implements OrderClient {

    @Override
    public String hello() {
        return "现在服务器忙，请稍后再试！";
    }

    @Override
    public String insertData() {
        return "现在服务器忙，请稍后再试！";
    }

}
