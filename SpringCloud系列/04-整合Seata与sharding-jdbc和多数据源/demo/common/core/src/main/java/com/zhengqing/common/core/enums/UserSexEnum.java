package com.zhengqing.common.core.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.google.common.collect.Lists;
import com.zhengqing.common.base.exception.MyException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 性别枚举类
 * </p>
 *
 * @author zhengqingya
 * @description 0->未知;1->男;2->女
 * @date 2020/11/28 22:56
 */
@Getter
@AllArgsConstructor
public enum UserSexEnum {

    /**
     * 未知
     */
    未知((byte) 0, "未知"),
    /**
     * 男
     */
    男((byte) 1, "男"),
    /**
     * 女
     */
    女((byte) 2, "女");

    /**
     * mybatis-plus 需配置扫包 `type-enums-package`
     * 类型值
     * {@link EnumValue} 标记数据库存的值是type
     */
    @EnumValue
    private final Byte type;
    /**
     * 类型描述
     * 标识前端展示
     */
//    @JsonValue
    private final String desc;


    private static final List<UserSexEnum> LIST = Lists.newArrayList();

    static {
        LIST.addAll(Arrays.asList(UserSexEnum.values()));
    }

    /**
     * 根据指定的规则类型查找相应枚举类
     *
     * @param type 类型
     * @return 类型枚举信息
     * @author zhengqingya
     * @date 2022/1/10 12:52
     */
    public static UserSexEnum getEnum(Byte type) {
        for (UserSexEnum itemEnum : LIST) {
            if (itemEnum.getType().equals(type)) {
                return itemEnum;
            }
        }
        throw new MyException("未找到指定类型数据！");
    }

}
