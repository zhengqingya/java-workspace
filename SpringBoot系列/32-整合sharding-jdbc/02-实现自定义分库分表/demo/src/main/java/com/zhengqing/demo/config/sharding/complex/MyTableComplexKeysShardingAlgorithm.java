package com.zhengqing.demo.config.sharding.complex;

import com.google.common.collect.Range;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <p>
 * 自定义分表规则类
 * </p>
 *
 * @author zhengqingya
 * @description 复合分片算法
 * @date 2021/11/1 11:19
 */
@Slf4j
public class MyTableComplexKeysShardingAlgorithm implements ComplexKeysShardingAlgorithm<Long> {

    @Override
    public Collection<String> doSharding(Collection<String> tableNameList, ComplexKeysShardingValue<Long> complexKeysShardingValue) {
        log.info("[MyTableComplexKeysShardingAlgorithm] complexKeysShardingValue: [{}]", complexKeysShardingValue);
        Set<String> tableNameResultList = new LinkedHashSet<>();
        int tableSize = tableNameList.size();
        // 用户id 范围查询
        Range<Long> rangeUserId = complexKeysShardingValue.getColumnNameAndRangeValuesMap().get("user_id");
        Long lower = rangeUserId.lowerEndpoint();
        Long upper = rangeUserId.upperEndpoint();
        // 根据user_id选择表 TODO ...
        for (String tableNameItem : tableNameList) {
            if (tableNameItem.endsWith(String.valueOf(lower % 2))
                    || tableNameItem.endsWith(String.valueOf(upper % 2))) {
                tableNameResultList.add(tableNameItem);
            }
            if (tableNameResultList.size() >= tableSize) {
                return tableNameResultList;
            }
        }
        return tableNameResultList;
    }

}
