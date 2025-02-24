package com.zhengqing.domain.user.aggregate;

import com.zhengqing.domain.user.core.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductEntity implements Entity {

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 商品 SPU ID
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品价格
     */
    private BigDecimal productPrice;

    // ...
}