package com.zhengqing.application.assembler;

import com.zhengqing.application.resp.OrderDetailResp;
import com.zhengqing.domain.user.aggregate.Order;

public class OrderApiAssembler {

    public static OrderDetailResp toDetailResp(Order order) {
        return OrderDetailResp.builder()
                .id(order.getId())
                .payAmount(order.getPayAmount())
                .payTime(order.getPayTime())
                .status(order.getStatus())
                // ...
                .build();
    }

}
