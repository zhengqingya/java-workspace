package com.zhengqing.demo.modules.weixin.model.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p> 菜单 - 基类 </p>
 *
 * @author : zhengqing
 * @description : 参考微信文档定义：https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Creating_Custom-Defined_Menu.html
 * @date : 2020/1/16 17:17
 */
@Data
@ApiModel(description = "菜单 - 基类")
public class Button {

    @ApiModelProperty(value = "菜单标题，不超过16个字节，子菜单不超过60个字节")
    private String name;

}
