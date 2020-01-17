package com.zhengqing.demo.modules.weixin.model.message.dto;

import com.zhengqing.demo.modules.weixin.enumeration.EnumRequestMessageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p> 图片消息 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/15 14:00
 */
@Data
@ApiModel(description = "图片消息")
public class ImageMessageDTO extends BaseMessageDTO {

    @ApiModelProperty(value = "消息类型", hidden = true)
    private String MsgType = EnumRequestMessageType.IMAGE.getType();

    @ApiModelProperty(value = "图片链接（由系统生成）")
    private String PicUrl;

    @ApiModelProperty(value = "图片消息媒体id，可以调用获取临时素材接口拉取数据")
    private String MediaId;

}
