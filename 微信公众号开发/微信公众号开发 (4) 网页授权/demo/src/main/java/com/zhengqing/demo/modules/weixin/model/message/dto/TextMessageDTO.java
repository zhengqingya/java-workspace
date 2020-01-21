package com.zhengqing.demo.modules.weixin.model.message.dto;

import com.zhengqing.demo.modules.weixin.enumeration.EnumRequestMessageType;
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
public class TextMessageDTO extends BaseMessageDTO {

    @ApiModelProperty(value = "消息类型", hidden = true)
    private String MsgType = EnumRequestMessageType.TEXT.getType();

    @ApiModelProperty(value = "文本消息内容")
    private String Content;

}
