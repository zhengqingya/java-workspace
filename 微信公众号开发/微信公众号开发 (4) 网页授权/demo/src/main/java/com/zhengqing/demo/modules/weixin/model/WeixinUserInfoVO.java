package com.zhengqing.demo.modules.weixin.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p> 微信用户基本信息 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/20 14:26
 */
@Data
@ApiModel(description = "微信用户基本信息")
public class WeixinUserInfoVO {

    @ApiModelProperty(value = "用户的唯一标识")
    private String openid;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "用户的性别，值为1时是男性，值为2时是女性，值为0时是未知")
    private Integer sex;

    @ApiModelProperty(value = "用户个人资料填写的省份")
    private String province;

    @ApiModelProperty(value = "普通用户个人资料填写的城市")
    private String city;

    @ApiModelProperty(value = "国家，如中国为CN")
    private String country;

    @ApiModelProperty(value = "用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。")
    private String headimgurl;

    @ApiModelProperty(value = "用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）")
    private String privilege;

    @ApiModelProperty(value = "只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。")
    private String unionid;

    @ApiModelProperty(value = "返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语")
    private String language;

}
