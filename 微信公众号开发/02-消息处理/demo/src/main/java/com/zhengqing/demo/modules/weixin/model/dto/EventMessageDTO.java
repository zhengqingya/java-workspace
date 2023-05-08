package com.zhengqing.demo.modules.weixin.model.dto;

import com.zhengqing.demo.modules.weixin.enumeration.EnumRequestMessageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p> 事件推送 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/15 14:00
 */
@Data
@ApiModel(description = "事件推送")
public class EventMessageDTO extends BaseMessageDTO {

    @ApiModelProperty(value = "消息类型", hidden = true)
    private String MsgType = EnumRequestMessageType.EVENT.getType();

    @ApiModelProperty(value = "文本消息内容")
    private String Event;

}
