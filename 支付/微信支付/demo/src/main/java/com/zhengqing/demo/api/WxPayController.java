package com.zhengqing.demo.api;

import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.zhengqing.demo.model.dto.PayOrderCreateDTO;
import com.zhengqing.demo.model.dto.PayOrderQueryDTO;
import com.zhengqing.demo.model.dto.PayOrderRefundDTO;
import com.zhengqing.demo.service.IPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * <p> 微信支付 </p>
 *
 * @author zhengqingya
 * @description 见 https://github.com/binarywang/weixin-java-pay-demo/blob/master/src/main/java/com/github/binarywang/demo/wx/pay/controller/WxPayController.java
 * @date 2023/4/21 15:36
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/pay")
@Api(tags = {"wx-支付"})
public class WxPayController {
    private final IPayService iPayService;

    @ApiOperation("创建支付订单")
    @PostMapping("createOrder")
    public Object createOrder(@Validated @RequestBody PayOrderCreateDTO params) throws Exception {
        return this.iPayService.unifiedOrder(params);
    }

    @ApiOperation("查询订单")
    @GetMapping("queryOrder")
    public WxPayOrderQueryResult queryOrder(@Validated @ModelAttribute PayOrderQueryDTO params) throws Exception {
        return this.iPayService.queryOrder(params);
    }

    @ApiOperation("退款")
    @PostMapping("refund")
    public Boolean refund(@Validated @RequestBody PayOrderRefundDTO params) throws Exception {
        return this.iPayService.refund(params);
    }


}

