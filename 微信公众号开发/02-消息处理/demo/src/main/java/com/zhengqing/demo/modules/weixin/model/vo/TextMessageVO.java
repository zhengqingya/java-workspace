package com.zhengqing.demo.modules.weixin.model.vo;

import com.zhengqing.demo.modules.weixin.enumeration.EnumResponseMessageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p> 文本消息 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/15 14:00
 */
@Data
@ApiModel(description = "文本消息")
public class TextMessageVO extends BaseMessageVO {

    @ApiModelProperty(value = "消息类型", hidden = true)
    private String MsgType = EnumResponseMessageType.TEXT.getType();

    @ApiModelProperty(value = "回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示）")
    private String Content;

}
