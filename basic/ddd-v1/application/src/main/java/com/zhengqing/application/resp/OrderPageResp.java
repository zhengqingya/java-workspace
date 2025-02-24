package com.zhengqing.application.resp;

import com.zhengqing.domain.user.enums.OrderStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p> 商城-订单信息-分页列表-响应参数 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/08/30 13:40
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("商城-订单信息-分页列表-响应参数")
public class OrderPageResp {

    @ApiModelProperty("订单id")
    private Long id;

    @ApiModelProperty("支付金额")
    private BigDecimal payAmount;

    @ApiModelProperty("支付时间")
    private Date payTime;

    @ApiModelProperty(value = "订单状态")
    private OrderStatusEnum status;

    // ...
}
