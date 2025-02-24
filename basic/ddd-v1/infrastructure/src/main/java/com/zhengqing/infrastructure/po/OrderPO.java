package com.zhengqing.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhengqing.domain.user.enums.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_oms_order")
public class OrderPO implements Serializable {
    private static final long serialVersionUID = 1L;

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

    /**
     * 创建时间
     */
    private Date createTime;

}