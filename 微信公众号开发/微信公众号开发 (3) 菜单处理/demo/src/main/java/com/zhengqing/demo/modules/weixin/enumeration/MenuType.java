package com.zhengqing.demo.modules.weixin.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p> 菜单类型 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/17 16:32
 */
@Getter
@AllArgsConstructor
public enum MenuType {

    // 点击式菜单
    CLICK("click"),
    // 链接式菜单
    VIEW("view");

    private String type;

}
