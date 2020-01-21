package com.zhengqing.demo.modules.weixin.model.menu;

import com.zhengqing.demo.modules.weixin.enumeration.MenuType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p> 点击式菜单 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/16 17:20
 */
@Data
@ApiModel(description = "用户点击菜单可接收消息推送")
public class ClickButton extends Button {

    @ApiModelProperty(value = "菜单的响应动作类型，view表示网页类型，click表示点击类型，miniprogram表示小程序类型")
    private String type = MenuType.CLICK.getType();

    @ApiModelProperty(value = "菜单KEY值，用于消息接口推送，不超过128字节")
    private String key;

}
