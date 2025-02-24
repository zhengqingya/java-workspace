package com.zhengqing.infrastructure.repository;

import com.zhengqing.domain.user.aggregate.Order;
import com.zhengqing.domain.user.repository.IOrderRepository;
import com.zhengqing.infrastructure.mapper.OrderMapper;
import com.zhengqing.infrastructure.po.OrderPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
@RequiredArgsConstructor
public class OrderRepository implements IOrderRepository {

    private final OrderMapper orderMapper;

    @Override
    public void insert(Order order) {
        orderMapper.insert(OrderPO.builder()
                .id(order.getId())
                .payAmount(order.getPayAmount())
                .payTime(order.getPayTime())
                .status(order.getStatus())
                .createTime(new Date())
                .build());
    }

    @Override
    public Order getById(Long id) {
        OrderPO orderPO = orderMapper.selectById(id);
        if (orderPO == null) {
            return null;
        }
        return Order.builder()
                .id(orderPO.getId())
                .payAmount(orderPO.getPayAmount())
                .payTime(orderPO.getPayTime())
                .status(orderPO.getStatus())
                .build();
    }
}
