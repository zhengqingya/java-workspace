package com.zhengqing.order.api;

import cn.hutool.core.util.RandomUtil;
import com.zhengqing.order.rpc.OrderRpcApi;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单api
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
@Api(tags = {"订单api"})
public class OrderController {

    // SPEL
    @Value("${spring.application.name}")
    private String name;

    @DubboReference
    private OrderRpcApi orderRpcApi;

    @GetMapping("/hello")
    public String hello() {
        // int result = 1 / 0;
        return "《order》:" + name;
    }

    @GetMapping("/detail")
    public String detail() {
        return "《RPC》:" + orderRpcApi.detail(RandomUtil.randomLong());
    }

}
