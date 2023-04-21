package com.zhengqing.demo.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p> 支付-订单创建-请求参数 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/08/20 17:38
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PayOrderCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty(value = "租户id", example = "1")
    private Integer tenantId;

    @NotBlank
    @ApiModelProperty(value = "内部系统订单号", example = "111")
    private String orderNo;

    @NotNull
    @ApiModelProperty(value = "总金额（单位：分）", example = "1")
    private Integer totalPrice;

    @NotBlank
    @ApiModelProperty(value = "订单描述", example = "测试商品")
    private String orderDesc;

    @ApiModelProperty(value = "用户openid", example = "1")
    private String openId;

    /**
     * {@link com.github.binarywang.wxpay.constant.WxPayConstants.TradeType}
     */
    @NotBlank
    @ApiModelProperty(value = "交易类型(JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付)", example = "JSAPI")
    private String tradeType;

}
