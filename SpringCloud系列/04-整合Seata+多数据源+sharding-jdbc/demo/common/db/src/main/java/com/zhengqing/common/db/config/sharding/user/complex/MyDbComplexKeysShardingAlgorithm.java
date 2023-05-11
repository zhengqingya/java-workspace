package com.zhengqing.common.db.config.sharding.user.complex;

import com.zhengqing.common.base.util.MyDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.util.*;

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

        // TODO
        Date createTime = (Date) this.getShardingValue(complexKeysShardingValue, "create_time");
        if (createTime.after(MyDateUtil.strToDate("2023-01-01 00:00:00", MyDateUtil.DATE_TIME_FORMAT))) {
            dbResultList.add("ds-master-0");
        } else {
            dbResultList.add("ds-master-1");
        }
        return dbResultList;
    }

    private Object getShardingValue(ComplexKeysShardingValue<String> shardingValues, final String key) {
        Map<String, Collection<String>> columnNameAndShardingValuesMap = shardingValues.getColumnNameAndShardingValuesMap();
        if (columnNameAndShardingValuesMap.containsKey(key)) {
            return columnNameAndShardingValuesMap.get(key).toArray()[0];
        }
        return null;
    }

}
