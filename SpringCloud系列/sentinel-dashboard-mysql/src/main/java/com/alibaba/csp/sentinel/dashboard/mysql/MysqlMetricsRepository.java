package com.alibaba.csp.sentinel.dashboard.mysql;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.MetricEntity;
import com.alibaba.csp.sentinel.dashboard.mysql.entity.SentinelMetricEntity;
import com.alibaba.csp.sentinel.dashboard.mysql.service.ISentinelMetricService;
import com.alibaba.csp.sentinel.dashboard.repository.metric.MetricsRepository;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p> Sentinel mysql持久化 </p>
 *
 * @author zhengqingya
 * @description 可参考内存持久化类 {@link com.alibaba.csp.sentinel.dashboard.repository.metric.InMemoryMetricsRepository}
 * @date 2021/10/3 8:03 下午
 */
@Slf4j
@Repository("MysqlMetricsRepository")
public class MysqlMetricsRepository implements MetricsRepository<MetricEntity> {

    @Autowired
    private ISentinelMetricService sentinelMetricService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(MetricEntity metric) {
        if (metric == null || StringUtil.isBlank(metric.getApp())) {
            return;
        }
//        log.info("MysqlMetricsRepository save: {}", metric);
        this.sentinelMetricService.save(this.copyProperties(metric, SentinelMetricEntity.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAll(Iterable<MetricEntity> metrics) {
        if (metrics == null) {
            return;
        }
        metrics.forEach(this::save);
    }

    @Override
    public List<MetricEntity> queryByAppAndResourceBetween(String app, String resource, long startTime, long endTime) {
        List<MetricEntity> resultList = new ArrayList<>();
        if (StringUtil.isBlank(app)) {
            return resultList;
        }
        if (StringUtil.isBlank(resource)) {
            return resultList;
        }
        List<SentinelMetricEntity> sentinelMetricEntityList = this.sentinelMetricService.list(
                new LambdaQueryWrapper<SentinelMetricEntity>()
                        .eq(SentinelMetricEntity::getApp, app)
                        .eq(SentinelMetricEntity::getResource, resource)
                        .ge(SentinelMetricEntity::getTimestamp, Date.from(Instant.ofEpochMilli(startTime)))
                        .le(SentinelMetricEntity::getTimestamp, Date.from(Instant.ofEpochMilli(endTime)))
        );
//        log.info("MysqlMetricsRepository queryByAppAndResourceBetween: {}", sentinelMetricEntityList);
        return this.copyList(sentinelMetricEntityList, MetricEntity.class);
    }

    @Override
    public List<String> listResourcesOfApp(String app) {
        List<String> resultList = new ArrayList<>();
        if (StringUtil.isBlank(app)) {
            return resultList;
        }
        final long startTime = System.currentTimeMillis() - 1000 * 60;
        List<SentinelMetricEntity> metricList = this.sentinelMetricService.list(
                new LambdaQueryWrapper<SentinelMetricEntity>()
                        .eq(SentinelMetricEntity::getApp, app)
                        .ge(SentinelMetricEntity::getTimestamp, Date.from(Instant.ofEpochMilli(startTime)))
        );
        if (CollectionUtils.isEmpty(metricList)) {
            return resultList;
        }

        Map<String, MetricEntity> resourceCount = new HashMap<>(32);
        metricList.forEach(newEntity -> {
            String resource = newEntity.getResource();
            if (resourceCount.containsKey(resource)) {
                MetricEntity oldEntity = resourceCount.get(resource);
                oldEntity.addPassQps(newEntity.getPassQps());
                oldEntity.addRtAndSuccessQps(newEntity.getRt(), newEntity.getSuccessQps());
                oldEntity.addBlockQps(newEntity.getBlockQps());
                oldEntity.addExceptionQps(newEntity.getExceptionQps());
                oldEntity.addCount(1);
            } else {
                newEntity.setResource(newEntity.getResource());
                resourceCount.put(resource, this.copyProperties(newEntity, MetricEntity.class));
            }
        });

        // Order by last minute b_qps DESC.
        return resourceCount.entrySet()
                .stream()
                .sorted((o1, o2) -> {
                    MetricEntity e1 = o1.getValue();
                    MetricEntity e2 = o2.getValue();
                    int t = e2.getBlockQps().compareTo(e1.getBlockQps());
                    if (t != 0) {
                        return t;
                    }
                    return e2.getPassQps().compareTo(e1.getPassQps());
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * 对象属性拷贝 : 将源对象的属性拷贝到目标对象
     *
     * @param source 源对象
     * @param clz    目标对象class
     * @return 对象数据
     */
    private <T> T copyProperties(Object source, Class<T> clz) {
        if (source == null) {
            return null;
        }
        T target = BeanUtils.instantiate(clz);
        try {
            BeanUtils.copyProperties(source, target);
        } catch (BeansException e) {
            log.error("BeanUtil property copy  failed :BeansException", e);
        } catch (Exception e) {
            log.error("BeanUtil property copy failed:Exception", e);
        }
        return target;
    }

    /**
     * 拷贝list
     *
     * @param inList 输入list
     * @param outClz 输出目标对象class
     * @return 返回集合
     */
    private <E, T> List<T> copyList(List<E> inList, Class<T> outClz) {
        List<T> output = new ArrayList<>();
        if (!CollectionUtils.isEmpty(inList)) {
            for (E source : inList) {
                T target = BeanUtils.instantiate(outClz);
                BeanUtils.copyProperties(source, target);
                output.add(target);
            }
        }
        return output;
    }

}
