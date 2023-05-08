package com.zhengqing.demo.modules.weixin.model.menu;

import com.zhengqing.demo.modules.weixin.enumeration.MenuType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p> 链接式菜单 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/16 17:20
 */
@Data
@ApiModel(description = "用户点击菜单可打开链接")
public class ViewButton extends Button {

    @ApiModelProperty(value = "菜单的响应动作类型，view表示网页类型，click表示点击类型，miniprogram表示小程序类型")
    private String type = MenuType.VIEW.getType();

    @ApiModelProperty(value = "(view、miniprogram类型必须) 网页 链接，用户点击菜单可打开链接，不超过1024字节。 type为miniprogram时，不支持小程序的老版本客户端将打开本url")
    private String url;

}
