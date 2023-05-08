package com.zhengqing.demo.modules.weixin.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p> access_token: 公众号的全局唯一接口调用凭据 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2Data1/16 16:46
 */
@Data
@ApiModel(description = "access_token: 公众号的全局唯一接口调用凭据")
public class AccessTokenVO {

    @ApiModelProperty(value = "获取到的凭证")
    private String access_token;

    @ApiModelProperty(value = "凭证有效时间，单位：秒(微信目前暂7200秒，即2小时，过期后需再次获取)")
    private int expires_in;

}
