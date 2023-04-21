package com.zhengqing.demo.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p> 支付-订单退款-请求参数 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/08/20 17:38
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PayOrderRefundDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    @NotNull
    @ApiModelProperty(value = "租户id", example = "1")
    private Integer tenantId;

    @ApiModelProperty(value = "内部系统订单号", example = "111")
    private String orderNo;

    @ApiModelProperty(value = "内部系统退款订单号", example = "111")
    private String refundOrderNo;

    @ApiModelProperty(value = "总金额（单位：分）", example = "1")
    private Integer totalPrice;

    @ApiModelProperty(value = "退款金额（单位：分）", example = "1")
    private Integer refundPrice;

    @ApiModelProperty(value = "退款描述", example = "其它原因")
    private String refundDesc;

}
