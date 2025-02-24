package com.zhengqing.domain.user.repository;

import com.zhengqing.domain.user.aggregate.Order;

public interface IOrderRepository {

    void insert(Order order);

    Order getById(Long id);

}
