package com.zhengqing.demo.factory;

import com.github.binarywang.wxpay.service.WxPayService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 微信支付
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/4/15 11:33
 */
@Slf4j
@Component
@AllArgsConstructor
public class WxPayFactory {

    private final WxPayService wxPayService;

    public WxPayService wxPayService() {
        // TODO 多商户模式下，在这里去切换...
        return this.wxPayService;
    }

}
