package com.zhengqing.demo.modules.weixin.model.message.vo;

import com.zhengqing.demo.modules.weixin.enumeration.EnumResponseMessageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p> 视频消息 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/15 17:13
 */
@Data
@ApiModel(description = "视频消息")
public class VideoMessageVO extends BaseMessageVO {

    @ApiModelProperty(value = "消息类型", hidden = true)
    private String MsgType = EnumResponseMessageType.VIDEO.getType();

    @ApiModelProperty(value = "视频")
    private Video Video;

    @Data
    @AllArgsConstructor
    @ApiModel(description = "视频消息中Voice类的定义")
    public static class Video {

        @ApiModelProperty(value = "通过素材管理中的接口上传多媒体文件，得到的id")
        private String MediaId;

        @ApiModelProperty(value = "视频消息的标题")
        private String Title;

        @ApiModelProperty(value = "视频消息的描述")
        private String Description;

    }

}
