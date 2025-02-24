package com.zhengqing.domain.user.repository;

import com.zhengqing.domain.user.aggregate.OrderProductEntity;

import java.util.List;

public interface IOrderProductRepository {
    List<OrderProductEntity> listByOrderId(Long orderId);

}
