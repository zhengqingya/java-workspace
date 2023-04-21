package com.zhengqing.demo.api;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.RandomUtil;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.util.SignUtils;
import com.zhengqing.demo.model.dto.PayOrderCreateDTO;
import com.zhengqing.demo.model.dto.PayOrderRefundDTO;
import com.zhengqing.demo.model.vo.PayOrderCreateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


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
    private final WxPayService wxPayService;

    @Value("${wx.pay.notifyHost:http://127.0.0.1}")
    private String notifyHost;

    @ApiOperation("创建支付订单")
    @PostMapping("createOrder")
    public Object createOrder(@Validated @RequestBody PayOrderCreateDTO params) throws Exception {
        String orderNo = params.getOrderNo();
        String tradeType = params.getTradeType();
        boolean isJsApi = WxPayConstants.TradeType.JSAPI.equals(tradeType);
        WxPayUnifiedOrderResult wxPayUnifiedOrderResult = this.wxPayService.unifiedOrder(
                WxPayUnifiedOrderRequest.newBuilder()
                        .outTradeNo(orderNo)
                        .openid(
                                // trade_type=JSAPI，此参数必传
                                isJsApi ? params.getOpenId() : ""
                        )
                        .totalFee(params.getTotalPrice())
                        .body(params.getOrderDesc())
                        .tradeType(tradeType)
                        .spbillCreateIp(NetUtil.getLocalhostStr())
                        .notifyUrl(this.notifyHost + "/wx/callback/notify/order/" + params.getTenantId())
                        .productId(orderNo)
                        .build()
        );
        if (WxPayConstants.TradeType.NATIVE.equals(tradeType)) {
            return wxPayUnifiedOrderResult.getCodeURL();
        } else if (WxPayConstants.TradeType.JSAPI.equals(tradeType)) {
            // 返回小程序需要的支付参数
            // 小程序调起支付 https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=5
            String nonceStr = RandomUtil.randomString(20);
            String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
            String wxPackage = "prepay_id=" + wxPayUnifiedOrderResult.getPrepayId();
            String signType = "MD5";
            String paySign = SignUtils.createSign(new HashMap<String, String>(5) {
                {
                    this.put("appId", wxPayUnifiedOrderResult.getAppid());
                    this.put("timeStamp", timeStamp);
                    this.put("nonceStr", nonceStr);
                    this.put("package", wxPackage);
                    this.put("signType", signType);
                }
            }, "MD5", this.wxPayService.getConfig().getMchKey(), null);

            return PayOrderCreateVO.builder()
                    .nonceStr(nonceStr)
                    .wxPackage(wxPackage)
                    .timeStamp(timeStamp)
                    .signType(signType)
                    .paySign(paySign)
                    .build();
        }
        return wxPayUnifiedOrderResult;
    }

    @ApiOperation("查询订单")
    @GetMapping("queryOrder")
    public WxPayOrderQueryResult queryOrder(@RequestParam String outTradeNo) throws Exception {
        return this.wxPayService.queryOrder(null, outTradeNo);
    }

    @ApiOperation("退款")
    @PostMapping("refund")
    public Boolean refund(@Validated @RequestBody PayOrderRefundDTO params) throws Exception {
        WxPayRefundResult wxPayRefundResult = this.wxPayService.refund(
                WxPayRefundRequest.newBuilder()
                        .outTradeNo(params.getOrderNo())
                        .outRefundNo(params.getRefundOrderNo())
                        .totalFee(params.getTotalPrice())
                        .refundFee(params.getRefundPrice())
                        .refundDesc(params.getRefundDesc())
                        .notifyUrl(this.notifyHost + "/wx/callback/notify/refund/" + params.getTenantId())
                        .build()
        );

        if ("SUCCESS".equals(wxPayRefundResult.getResultCode())) {
            return true;
        }

        throw new Exception(wxPayRefundResult.getErrCodeDes());
    }


}

