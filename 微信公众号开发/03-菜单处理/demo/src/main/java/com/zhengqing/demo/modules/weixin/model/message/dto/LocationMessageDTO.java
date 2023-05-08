package com.zhengqing.demo.modules.weixin.model.message.dto;

import com.zhengqing.demo.modules.weixin.enumeration.EnumRequestMessageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p> 地理位置消息 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/15 17:13
 */
@Data
@ApiModel(description = "地理位置消息")
public class LocationMessageDTO extends BaseMessageDTO {

    @ApiModelProperty(value = "消息类型", hidden = true)
    private String MsgType = EnumRequestMessageType.LOCATION.getType();

    @ApiModelProperty(value = "地理位置维度")
    private String Location_X;

    @ApiModelProperty(value = "地理位置经度")
    private String Location_Y;

    @ApiModelProperty(value = "地图缩放大小")
    private String Scale;

    @ApiModelProperty(value = "地理位置信息")
    private String Label;

}
