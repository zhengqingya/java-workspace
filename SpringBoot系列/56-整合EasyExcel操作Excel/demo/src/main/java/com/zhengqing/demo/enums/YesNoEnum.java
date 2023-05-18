package com.zhengqing.demo.enums;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

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
public enum YesNoEnum {

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
    private final int value;
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
    @SneakyThrows(Exception.class)
    public static YesNoEnum getEnum(int value) {
        for (YesNoEnum itemEnum : LIST) {
            if (itemEnum.getValue() == value) {
                return itemEnum;
            }
        }
        throw new Exception("未找到指定类型枚举数据！");
    }

}
