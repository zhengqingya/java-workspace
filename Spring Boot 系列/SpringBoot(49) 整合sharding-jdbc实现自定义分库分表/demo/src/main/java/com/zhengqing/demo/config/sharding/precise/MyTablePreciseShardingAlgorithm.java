package com.zhengqing.demo.config.sharding.precise;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * <p> 自定义分表规则类 </p>
 *
 * @author zhengqingya
 * @description 精确分片算法
 * @date 2021/10/30 1:14 下午
 */
@Slf4j
public class MyTablePreciseShardingAlgorithm implements PreciseShardingAlgorithm<Byte> {

    /**
     * 分片策略
     *
     * @param tableNameList 所有表名
     * @param shardingValue SQL执行时传入的分片值
     * @return 表名
     */
    @Override
    public String doSharding(Collection<String> tableNameList, PreciseShardingValue<Byte> shardingValue) {
        log.info("[MyTablePreciseShardingAlgorithm] SQL执行时传入的分片值: [{}]", shardingValue);
        // 根据用户性别取模拆分到不同的表中
        Byte sex = shardingValue.getValue();
        for (String tableNameItem : tableNameList) {
            if (tableNameItem.endsWith(String.valueOf(sex % 2))) {
                return tableNameItem;
            }
        }
        return null;
    }

}
