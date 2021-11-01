package com.zhengqing.demo.config.sharding.range;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.util.Collection;
import java.util.List;


/**
 * <p> 自定义分库规则类 </p>
 *
 * @author zhengqingya
 * @description 范围分片算法
 * @date 2021/11/1 10:37
 */
@Slf4j
public class MyDbRangeShardingAlgorithm implements RangeShardingAlgorithm<Long> {

    @Override
    public Collection<String> doSharding(Collection<String> dbNameList, RangeShardingValue<Long> shardingValue) {
        log.info("[MyDbRangeShardingAlgorithm] shardingValue: [{}]", shardingValue);
        List<String> result = Lists.newLinkedList();
        int dbSize = dbNameList.size();
        // 从sql 中获取 Between 1 and 1000 的值
        // lower：1
        // upper：1000
        Range<Long> rangeValue = shardingValue.getValueRange();
        Long lower = rangeValue.lowerEndpoint();
        Long upper = rangeValue.upperEndpoint();
        // 根据范围值取偶选择库
        for (Long i = lower; i <= upper; i++) {
            for (String dbNameItem : dbNameList) {
                if (dbNameItem.endsWith(String.valueOf(i % 2))) {
                    result.add(dbNameItem);
                }
                if (result.size() >= dbSize) {
                    return result;
                }
            }
        }
        return result;
    }

}
