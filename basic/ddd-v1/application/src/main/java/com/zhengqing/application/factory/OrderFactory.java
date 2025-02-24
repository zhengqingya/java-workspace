package com.zhengqing.application.factory;

import com.zhengqing.domain.user.aggregate.Order;

public class OrderFactory {

    // 构建复杂对象使用...
    public Order buildOrder() {
        return Order.builder().build();
    }

}
