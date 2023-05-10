package com.zhengqing.common.base.enums;

import com.google.common.collect.Lists;
import com.zhengqing.common.base.exception.MyException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 是/否
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/4/12 0:01
 */
@Getter
@AllArgsConstructor
public enum YesNoEnum implements IBaseEnum<Integer> {

    /**
     * 是
     */
    YES(1, "是"),
    /**
     * 否
     */
    NO(0, "否");

    /**
     * 类型
     */
    private final Integer value;
    /**
     * 描述
     */
    private final String desc;

    private static final List<YesNoEnum> LIST = Lists.newArrayList();

    static {
        LIST.addAll(Arrays.asList(YesNoEnum.values()));
    }

    /**
     * 根据指定类型查找相应枚举类
     */
    public static YesNoEnum getEnum(Integer value) {
        for (YesNoEnum itemEnum : LIST) {
            if (itemEnum.getValue().equals(value)) {
                return itemEnum;
            }
        }
        throw new MyException("未找到指定类型枚举数据！");
    }

}
