package com.zhengqing.demo.config.sharding.precise;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * <p> 自定义分库规则类 </p>
 *
 * @author zhengqingya
 * @description 精确分片算法
 * @date 2021/10/30 1:12 下午
 */
@Slf4j
public class MyDbPreciseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    /**
     * 分片策略
     *
     * @param dbNameList    所有数据源
     * @param shardingValue SQL执行时传入的分片值
     * @return 数据源名称
     */
    @Override
    public String doSharding(Collection<String> dbNameList, PreciseShardingValue<Long> shardingValue) {
        log.info("[MyDbPreciseShardingAlgorithm] SQL执行时传入的分片值: [{}]", shardingValue);
        // 根据user_id取模拆分到不同的库中
        Long userId = shardingValue.getValue();
        for (String dbNameItem : dbNameList) {
            if (dbNameItem.endsWith(String.valueOf(userId % 2))) {
                return dbNameItem;
            }
        }
        return null;
    }

}
