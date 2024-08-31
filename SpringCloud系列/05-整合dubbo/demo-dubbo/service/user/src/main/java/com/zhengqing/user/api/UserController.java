package com.zhengqing.user.api;

import com.zhengqing.order.rpc.OrderRpcApi;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Api(tags = {"用户api"})
public class UserController {

    @DubboReference
    private OrderRpcApi orderRpcApi;

    @GetMapping("/hello")
    public Object hello(@RequestParam Long id) {
        return this.orderRpcApi.detail(id);
    }

}
