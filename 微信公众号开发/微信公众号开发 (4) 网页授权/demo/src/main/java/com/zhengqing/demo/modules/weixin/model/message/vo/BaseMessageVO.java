package com.zhengqing.demo.modules.weixin.model.message.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p> 响应消息-基类 （公众号 -> 普通用户） </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/15 13:54
 */
@Data
@ApiModel(description = "响应消息-基类")
public class BaseMessageVO {

    @ApiModelProperty(value = "接收方帐号（收到的OpenID）")
    private String ToUserName;

    @ApiModelProperty(value = "开发者微信号")
    private String FromUserName;

    @ApiModelProperty(value = "消息创建时间 （整型）")
    private long CreateTime;

//    @ApiModelProperty(value = "消息类型（text/image/voice/video/music/news）")
//    private String MsgType;

    @ApiModelProperty(value = "位0x0001被标志时，星标刚收到的消息")
    private int FuncFlag = 0;

}
