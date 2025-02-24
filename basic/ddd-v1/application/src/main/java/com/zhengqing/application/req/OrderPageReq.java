package com.zhengqing.application.req;

import com.zhengqing.domain.user.enums.OrderStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p> 商城-订单信息-分页列表-请求参数 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/08/30 13:40
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("商城-订单信息-分页列表-请求参数")
public class OrderPageReq extends BasePageReq {

    @ApiModelProperty("搜索关键字（订单号或商品名称）")
    private String keyWord;

    @ApiModelProperty("订单id")
    private Long id;

    @ApiModelProperty(value = "订单状态")
    private OrderStatusEnum status;

}
