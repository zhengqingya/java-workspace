package com.zhengqing.demo.alipay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p> 支付成功-回调参数 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/7/10 6:51 下午
 */
@Data
@ApiModel("支付成功-回调参数")
public class AlipaySuccessDTO {

    @ApiModelProperty(value = "商户订单号")
    private String out_trade_no;

    @ApiModelProperty(value = "支付宝交易号")
    private String trade_no;

    @ApiModelProperty(value = "付款金额")
    private BigDecimal total_amount;

}
