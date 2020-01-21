package com.zhengqing.demo.modules.weixin.model.message.dto;

import com.zhengqing.demo.modules.weixin.enumeration.EnumRequestMessageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p> 小视频消息 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/15 17:13
 */
@Data
@ApiModel(description = "小视频消息")
public class ShortVideoMessageDTO extends BaseMessageDTO {

    @ApiModelProperty(value = "消息类型", hidden = true)
    private String MsgType = EnumRequestMessageType.SHORTVIDEO.getType();

    @ApiModelProperty(value = "视频消息媒体id，可以调用获取临时素材接口拉取数据")
    private String MediaId;

    @ApiModelProperty(value = "视频消息缩略图的媒体id，可以调用获取临时素材接口拉取数据")
    private String ThumbMediaId;

}
