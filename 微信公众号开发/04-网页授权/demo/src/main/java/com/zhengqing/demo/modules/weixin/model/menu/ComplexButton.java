package com.zhengqing.demo.modules.weixin.model.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p> 含二级菜单的一级菜单 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/16 17:21
 */
@Data
@ApiModel(description = "含二级菜单的一级菜单")
public class ComplexButton extends Button {

    @ApiModelProperty(value = "二级菜单数组，个数应为1~5个")
    private Button[] sub_button;

}
