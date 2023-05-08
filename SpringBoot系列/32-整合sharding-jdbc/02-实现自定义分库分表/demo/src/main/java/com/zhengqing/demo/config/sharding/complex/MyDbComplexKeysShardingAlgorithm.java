package com.zhengqing.demo.config.sharding.complex;

import com.google.common.collect.Range;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 自定义分库规则类
 * </p>
 *
 * @author zhengqingya
 * @description 复合分片算法
 * @date 2021/11/1 11:19
 */
@Slf4j
public class MyDbComplexKeysShardingAlgorithm implements ComplexKeysShardingAlgorithm<String> {

    @Override
    public Collection<String> doSharding(Collection<String> dbNameList, ComplexKeysShardingValue<String> complexKeysShardingValue) {
        log.info("[MyDbComplexKeysShardingAlgorithm] complexKeysShardingValue: [{}]", complexKeysShardingValue);
        List<String> dbResultList = new ArrayList<>();
        int dbSize = dbNameList.size();
        // 得到每个分片健对应的值
        // 用户id 范围查询
        Range<String> rangeUserId = complexKeysShardingValue.getColumnNameAndRangeValuesMap().get("user_id");
        // 性别
        List<String> sexValueList = this.getShardingValue(complexKeysShardingValue, "sex");
        // 对两个分片健进行逻辑操作，选择最终数据进哪一库？ TODO
        for (String sex : sexValueList) {
            String suffix = String.valueOf(Long.parseLong(sex) % 2);
            for (String dbNameItem : dbNameList) {
                if (dbNameItem.endsWith(suffix)) {
                    dbResultList.add(dbNameItem);
                }
                if (dbResultList.size() >= dbSize) {
                    return dbResultList;
                }
            }
        }
        return dbResultList;
    }

    private List<String> getShardingValue(ComplexKeysShardingValue<String> shardingValues, final String key) {
        List<String> valueList = new ArrayList<>();
        Map<String, Collection<String>> columnNameAndShardingValuesMap = shardingValues.getColumnNameAndShardingValuesMap();
        if (columnNameAndShardingValuesMap.containsKey(key)) {
            valueList.addAll(columnNameAndShardingValuesMap.get(key));
        }
        return valueList;
    }

}
