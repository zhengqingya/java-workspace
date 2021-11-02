package com.zhengqing.demo.config.sharding.hint;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingValue;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <p> 自定义分表规则类 </p>
 *
 * @author zhengqingya
 * @description Hint分片算法
 * @date 2021/11/1 16:09
 */
@Slf4j
public class MyTableHintShardingAlgorithm implements HintShardingAlgorithm<Integer> {

    @Override
    public Collection<String> doSharding(Collection<String> tableNameList, HintShardingValue<Integer> hintShardingValue) {
        log.info("[MyTableHintShardingAlgorithm] hintShardingValue: [{}]", hintShardingValue);
        Collection<String> tableResultList = new ArrayList<>();
        int tableSize = tableNameList.size();
        Collection<Integer> hintShardingValueValueList = hintShardingValue.getValues();
        for (String tableName : tableNameList) {
            for (Integer shardingValue : hintShardingValueValueList) {
                if (tableName.endsWith(String.valueOf(shardingValue % 2))) {
                    tableResultList.add(tableName);
                }
                if (tableResultList.size() >= tableSize) {
                    return tableResultList;
                }
            }
        }
        return tableResultList;
    }

}
