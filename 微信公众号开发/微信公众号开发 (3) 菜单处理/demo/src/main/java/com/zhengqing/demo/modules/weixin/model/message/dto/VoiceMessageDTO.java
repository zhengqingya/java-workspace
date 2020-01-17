package com.zhengqing.demo.modules.weixin.model.message.dto;

import com.zhengqing.demo.modules.weixin.enumeration.EnumRequestMessageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
public class VoiceMessageDTO extends BaseMessageDTO {

    @ApiModelProperty(value = "消息类型", hidden = true)
    private String MsgType = EnumRequestMessageType.VOICE.getType();

    @ApiModelProperty(value = "语音消息媒体id，可以调用获取临时素材接口拉取数据")
    private String MediaId;

    @ApiModelProperty(value = "语音格式，如amr，speex等")
    private String Format;

    /**
     * 请注意，开通语音识别后，用户每次发送语音给公众号时，微信会在推送的语音消息XML数据包中，
     * 增加一个Recognition字段（注：由于客户端缓存，开发者开启或者关闭语音识别功能，对新关注者立刻生效，对已关注用户需要24小时生效。开发者可以重新关注此帐号进行测试）
     */
    @ApiModelProperty(value = "语音识别结果，UTF8编码")
    private String Recognition;

}
