package com.zhengqing.common.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p> 认证来源枚举 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/6/10 15:18
 */
@Getter
@AllArgsConstructor
public enum AuthSourceEnum implements IBaseEnum<String> {

    B("B", "B端系统用户"),
    C("C", "C端用户");

    private String value;
    private String desc;

}
