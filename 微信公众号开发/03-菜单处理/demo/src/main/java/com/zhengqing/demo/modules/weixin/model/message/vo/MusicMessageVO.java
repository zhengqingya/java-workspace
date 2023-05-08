package com.zhengqing.demo.modules.weixin.model.message.vo;

import com.zhengqing.demo.modules.weixin.enumeration.EnumResponseMessageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p> 音乐消息 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/15 14:04
 */
@Data
@ApiModel(description = "音乐消息")
public class MusicMessageVO extends BaseMessageVO {

    @ApiModelProperty(value = "消息类型", hidden = true)
    private String MsgType = EnumResponseMessageType.MUSIC.getType();

    @ApiModelProperty(value = "音乐")
    private Music Music;

    @Data
    @AllArgsConstructor
    @ApiModel(description = "音乐消息中Music类的定义")
    public static class Music {

        @ApiModelProperty(value = "音乐标题")
        private String Title;

        @ApiModelProperty(value = "音乐描述")
        private String Description;

        @ApiModelProperty(value = "音乐链接")
        private String MusicUrl;

        @ApiModelProperty(value = "高质量音乐链接，WIFI环境优先使用该链接播放音乐")
        private String HQMusicUrl;

        @ApiModelProperty(value = "缩略图的媒体id，通过素材管理中的接口上传多媒体文件，得到的id")
        private String ThumbMediaId;

    }

}
