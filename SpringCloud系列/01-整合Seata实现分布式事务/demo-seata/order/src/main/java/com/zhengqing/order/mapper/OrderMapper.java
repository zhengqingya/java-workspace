package com.zhengqing.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhengqing.order.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 订单 Mapper 接口
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 批量插入数据
     *
     * @param list list
     * @return void
     * @author zhengqingya
     * @date 2021/5/28 14:28
     */
    void insertBatch(@Param("list") List<Order> list);

}
