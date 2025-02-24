package com.zhengqing.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhengqing.domain.user.aggregate.OrderProductEntity;
import com.zhengqing.domain.user.repository.IOrderProductRepository;
import com.zhengqing.infrastructure.mapper.OrderProductMapper;
import com.zhengqing.infrastructure.po.OrderProductPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderProductRepository implements IOrderProductRepository {

    private final OrderProductMapper orderProductMapper;

    @Override
    public List<OrderProductEntity> listByOrderId(Long orderId) {
        List<OrderProductPO> list = orderProductMapper.selectList(
                new LambdaQueryWrapper<OrderProductPO>()
                        .eq(OrderProductPO::getOrderId, orderId)
        );
        return list.stream()
                .map(e -> OrderProductEntity.builder()
                        .orderId(e.getOrderId())
                        .productId(e.getProductId())
                        .productName(e.getProductName())
                        .productPrice(e.getProductPrice())
                        .build())
                .collect(Collectors.toList());
    }
}
