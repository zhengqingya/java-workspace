package com.zhengqing.demo.modules.weixin.model.message.vo;

import com.zhengqing.demo.modules.weixin.enumeration.EnumResponseMessageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
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
public class ImageMessageVO extends BaseMessageVO {

    @ApiModelProperty(value = "消息类型", hidden = true)
    private String MsgType = EnumResponseMessageType.IMAGE.getType();

    @ApiModelProperty(value = "图片")
    private Image Image;

    @Data
    @AllArgsConstructor
    @ApiModel(description = "图片消息中Image类的定义")
    public static class Image {

        @ApiModelProperty(value = "通过素材管理中的接口上传多媒体文件，得到的id")
        private String MediaId;

    }

}
