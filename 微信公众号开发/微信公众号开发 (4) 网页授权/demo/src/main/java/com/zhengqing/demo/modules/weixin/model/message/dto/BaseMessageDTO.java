package com.zhengqing.demo.modules.weixin.model.message.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p> 请求消息-基类 （ 普通用户 -> 公众号 ） </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/15 13:54
 */
@Data
@ApiModel(description = "请求消息-基类")
public class BaseMessageDTO {

    @ApiModelProperty(value = "开发者微信号")
    private String ToUserName;

    @ApiModelProperty(value = "发送方帐号（一个OpenID）")
    private String FromUserName;

    @ApiModelProperty(value = "消息创建时间 （整型）")
    private long CreateTime;

//    @ApiModelProperty(value = "消息类型（text/image/voice/video/shortvideo/location/link）")
//    private String MsgType;

    @ApiModelProperty(value = "消息id，64位整型")
    private long MsgId;

}
