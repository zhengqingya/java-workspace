package com.zhengqing.demo.modules.weixin.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p> 微信响应结果 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/17 11:32
 */
@Data
@ApiModel(description = "微信响应结果")
public class WeixinResponseResult {

    @ApiModelProperty(value = "响应码")
    private int errcode;

    @ApiModelProperty(value = "响应消息")
    private String errmsg;

}
