package com.zhengqing.application.service.impl;

import cn.hutool.core.lang.Assert;
import com.zhengqing.application.assembler.OrderApiAssembler;
import com.zhengqing.application.resp.OrderDetailResp;
import com.zhengqing.application.service.IOrderService;
import com.zhengqing.domain.user.aggregate.Order;
import com.zhengqing.domain.user.aggregate.OrderProductEntity;
import com.zhengqing.domain.user.repository.IOrderProductRepository;
import com.zhengqing.domain.user.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final IOrderRepository iOrderRepository;
    private final IOrderProductRepository iOrderProductRepository;

    @Override
    public OrderDetailResp detail(Long id) {
        // 1、校验数据
        Assert.notNull(id, "id不能为空");
        // ...

        // 2、查询数据
        Order order = iOrderRepository.getById(id);
        List<OrderProductEntity> productEntityList = iOrderProductRepository.listByOrderId(id);
        order.setOrderProducts(productEntityList);

        // 3、处理其它业务...
        return OrderApiAssembler.toDetailResp(order);
    }

}
