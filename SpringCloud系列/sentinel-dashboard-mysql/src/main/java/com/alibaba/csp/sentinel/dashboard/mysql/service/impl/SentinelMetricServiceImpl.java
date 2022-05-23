package com.alibaba.csp.sentinel.dashboard.mysql.service.impl;

import com.alibaba.csp.sentinel.dashboard.mysql.entity.SentinelMetricEntity;
import com.alibaba.csp.sentinel.dashboard.mysql.mapper.SentinelMetricMapper;
import com.alibaba.csp.sentinel.dashboard.mysql.service.ISentinelMetricService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p> Sentinel 服务实现类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/10/03 18:47
 */
@Slf4j
@Service
public class SentinelMetricServiceImpl extends ServiceImpl<SentinelMetricMapper, SentinelMetricEntity> implements ISentinelMetricService {

}
