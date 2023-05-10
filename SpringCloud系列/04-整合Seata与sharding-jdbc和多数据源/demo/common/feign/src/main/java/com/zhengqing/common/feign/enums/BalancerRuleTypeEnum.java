package com.zhengqing.common.feign.enums;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 自定义负载均衡策略-规则类型枚举类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/9/13 18:46
 */
@Slf4j
@Getter
@AllArgsConstructor
public enum BalancerRuleTypeEnum {

    /**
     * 权重
     */
    WEIGHT("weight", "权重"),
    /**
     * 同一集群使用相同版本
     */
    VERSION("version", "同一集群使用相同版本"),
    /**
     * 同一集群无相同版本，使用权重
     */
    VERSION_WEIGHT("version_weight", "同一集群无相同版本，使用权重"),
    ;

    /**
     * 规则类型
     */
    private final String type;
    /**
     * 规则描述
     */
    private final String desc;

    private static final List<BalancerRuleTypeEnum> LIST = Lists.newArrayList();

    static {
        LIST.addAll(Arrays.asList(BalancerRuleTypeEnum.values()));
    }

    /**
     * 根据指定的规则类型查找相应枚举类
     *
     * @param type 规则类型
     * @return 规则类型枚举信息
     * @author zhengqingya
     * @date 2020/9/13 18:46
     */
    public static BalancerRuleTypeEnum getEnum(String type) {
        for (BalancerRuleTypeEnum itemEnum : LIST) {
            if (itemEnum.getType().equals(type)) {
                return itemEnum;
            }
        }
        log.warn("未找到指定的负载均衡策略，默认权重策略!");
        return WEIGHT;
    }

}
