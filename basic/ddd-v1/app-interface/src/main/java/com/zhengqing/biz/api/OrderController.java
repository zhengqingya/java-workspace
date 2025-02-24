package com.zhengqing.biz.api;

import com.zhengqing.application.resp.OrderDetailResp;
import com.zhengqing.application.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = {"订单api"})
public class OrderController {

    private final IOrderService iOrderService;

    @ApiOperation("详情")
    @GetMapping("/api/order/{id}") // http://127.0.0.1:10020/api/order/1
    public OrderDetailResp detail(@PathVariable("id") Long id) {
        return this.iOrderService.detail(id);
    }

}
