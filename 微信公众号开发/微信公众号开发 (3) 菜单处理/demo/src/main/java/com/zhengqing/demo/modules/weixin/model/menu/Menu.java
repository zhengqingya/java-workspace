package com.zhengqing.demo.modules.weixin.model.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p> 菜单树 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/16 17:21
 */
@Data
@ApiModel(description = "菜单树")
public class Menu {

    @ApiModelProperty(value = "一级菜单数组，个数应为1~3个")
    private Button[] button;

}
