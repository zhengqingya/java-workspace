package com.zhengqing.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhengqing.infrastructure.po.OrderPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper extends BaseMapper<OrderPO> {

    @Select("select * from t_oms_order where id = #{id}")
    OrderPO selectById(Long id);

}