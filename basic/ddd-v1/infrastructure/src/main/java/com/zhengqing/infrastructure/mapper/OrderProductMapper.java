package com.zhengqing.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhengqing.infrastructure.po.OrderProductPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderProductMapper extends BaseMapper<OrderProductPO> {


}