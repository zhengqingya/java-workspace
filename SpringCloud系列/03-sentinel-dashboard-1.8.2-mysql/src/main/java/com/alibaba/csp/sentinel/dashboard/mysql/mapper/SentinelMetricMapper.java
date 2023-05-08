package com.alibaba.csp.sentinel.dashboard.mysql.mapper;

import com.alibaba.csp.sentinel.dashboard.mysql.entity.SentinelMetricEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mybatis.spring.annotation.MapperScan;

/**
 * <p> Sentinel Mapper </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/10/03 18:47
 */
@MapperScan
public interface SentinelMetricMapper extends BaseMapper<SentinelMetricEntity> {

}
