package com.zhengqing.demo.config.sharding.hint;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingValue;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <p> 自定义分库规则类 </p>
 *
 * @author zhengqingya
 * @description Hint分片算法
 * @date 2021/11/1 16:09
 */
@Slf4j
public class MyDbHintShardingAlgorithm implements HintShardingAlgorithm<Integer> {

    @Override
    public Collection<String> doSharding(Collection<String> dbNameList, HintShardingValue<Integer> hintShardingValue) {
        log.info("[MyDbHintShardingAlgorithm] hintShardingValue: [{}]", hintShardingValue);
        Collection<String> dbResultList = new ArrayList<>();
        int dbSize = dbNameList.size();
        for (String dbNameItem : dbNameList) {
            for (Integer shardingValue : hintShardingValue.getValues()) {
                if (dbNameItem.endsWith(String.valueOf(shardingValue % 2))) {
                    dbResultList.add(dbNameItem);
                }
                if (dbResultList.size() >= dbSize) {
                    return dbResultList;
                }
            }
        }
        return dbResultList;
    }
}
