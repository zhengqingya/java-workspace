package com.zhengqing.common.db.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p> 数据是否删除枚举 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/6/11 18:29
 */
@Getter
@AllArgsConstructor
public enum IsDeletedEnum {

    /**
     * 删除
     */
    YES(1, "删除"),
    /**
     * 未删除
     */
    NO(0, "未删除");

    /**
     * 值
     */
    private final Integer value;
    /**
     * 描述
     */
    private final String desc;

}
