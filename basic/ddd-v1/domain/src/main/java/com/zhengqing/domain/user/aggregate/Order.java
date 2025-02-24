package com.zhengqing.domain.user.aggregate;

import com.zhengqing.domain.user.core.AggregateRoot;
import com.zhengqing.domain.user.enums.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order implements AggregateRoot {

    /**
     * 订单id
     */
    private Long id;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 订单状态
     */
    private OrderStatusEnum status;

    // ...

    /**
     * 订单商品集合
     */
    private List<OrderProductEntity> orderProducts;

}