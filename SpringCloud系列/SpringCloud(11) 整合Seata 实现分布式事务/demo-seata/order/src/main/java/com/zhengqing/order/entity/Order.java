package com.zhengqing.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

/**
 * <p>
 * 订单
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_order")
@ApiModel("订单")
@EqualsAndHashCode(callSuper = true)
public class Order extends Model<Order> {

    @ApiModelProperty("订单ID")
    @TableId(type = IdType.AUTO)
    private Long orderId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("支付时间")
    private Date payTime;

    @ApiModelProperty("支付金额")
    private Integer payMoney;

    @ApiModelProperty("备注")
    private String remark;

}
