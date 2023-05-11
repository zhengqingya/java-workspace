package com.zhengqing.user.feign;

import com.zhengqing.user.feign.fallback.OrderFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "order", fallback = OrderFallback.class)
public interface OrderClient {

    @GetMapping("/api/order/hello")
    String hello();


    @PostMapping("/api/order")
    String insertData();

}
