package com.zhengqing.order.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhengqing.common.db.config.dynamic.DataSourceConfig;
import com.zhengqing.order.entity.Order;
import com.zhengqing.order.mapper.OrderMapper;
import com.zhengqing.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
@Slf4j
@Service
@DS(DataSourceConfig.SHARDING_DATA_SOURCE_NAME)
public class OrderServiceImpl extends ServiceImpl<com.zhengqing.order.mapper.OrderMapper, Order> implements IOrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void insertBatch(List<Order> list) {
        this.orderMapper.insertBatch(list);
    }

}
