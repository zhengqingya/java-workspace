package com.zhengqing.common.base.enums;


import cn.hutool.core.util.ObjectUtil;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p> 通用枚举接口 </p>
 *
 * @author zhengqingya
 * @description 符号 T、E、？代表的意思:
 * T 表示一种特定的类型
 * E 也是一种类型的意思，只不过通常代表集合中的元素
 * ? 这是一种无限的符号，代表任何类型都可以
 * @date 2019/8/22 11:00
 */
public interface IBaseEnum<T> {

    /**
     * 值
     */
    T getValue();

    /**
     * 描述
     */
    String getDesc();

    /**
     * 获取所有枚举值
     */
    static <E extends Enum<E>> List<E> list(Class<E> clazz) {
        EnumSet<E> allEnums = EnumSet.allOf(clazz);
        return allEnums.stream().collect(Collectors.toList());
    }

    /**
     * 根据值获取枚举
     */
    static <E extends Enum<E> & IBaseEnum> E getEnumByValue(Object value, Class<E> clazz) {
        Objects.requireNonNull(value);
        EnumSet<E> allEnums = EnumSet.allOf(clazz);
        E matchEnum = allEnums.stream()
                .filter(e -> ObjectUtil.equal(e.getValue(), value))
                .findFirst()
                .orElse(null);
        return matchEnum;
    }

    /**
     * 根据值获取描述
     */
    static <E extends Enum<E> & IBaseEnum> Object getValueByDesc(String desc, Class<E> clazz) {
        Objects.requireNonNull(desc);
        EnumSet<E> allEnums = EnumSet.allOf(clazz);
        String finalDesc = desc;
        E matchEnum = allEnums.stream()
                .filter(e -> ObjectUtil.equal(e.getDesc(), finalDesc))
                .findFirst()
                .orElse(null);

        Object value = null;
        if (matchEnum != null) {
            value = matchEnum.getValue();
        }
        return value;
    }

    /**
     * 根据描述获取值
     */
    static <E extends Enum<E> & IBaseEnum> String getDescByValue(Object value, Class<E> clazz) {
        Objects.requireNonNull(value);
        EnumSet<E> allEnums = EnumSet.allOf(clazz);
        E matchEnum = allEnums.stream()
                .filter(e -> ObjectUtil.equal(e.getValue(), value))
                .findFirst()
                .orElse(null);
        String desc = null;
        if (matchEnum != null) {
            desc = matchEnum.getDesc();
        }
        return desc;
    }

}
