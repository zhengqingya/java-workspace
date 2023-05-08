package com.zhengqing.demo.modules.weixin.model.message.vo;

import com.zhengqing.demo.modules.weixin.enumeration.EnumResponseMessageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p> 语音消息 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/15 17:13
 */
@Data
@ApiModel(description = "语音消息")
public class VoiceMessageVO extends BaseMessageVO {

    @ApiModelProperty(value = "消息类型", hidden = true)
    private String MsgType = EnumResponseMessageType.VOICE.getType();

    @ApiModelProperty(value = "语音")
    private Voice Voice;

    @Data
    @AllArgsConstructor
    @ApiModel(description = "语音消息中Voice类的定义")
    public static class Voice {

        @ApiModelProperty(value = "通过素材管理中的接口上传多媒体文件，得到的id")
        private String MediaId;

    }

}
