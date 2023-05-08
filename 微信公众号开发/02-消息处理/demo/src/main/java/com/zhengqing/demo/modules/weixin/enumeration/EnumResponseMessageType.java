package com.zhengqing.demo.modules.weixin.enumeration;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p> 响应消息类型枚举类 </p>
 *
 * @author : zhengqing
 * @description : 可参考微信开放文档定义：https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Passive_user_reply_message.html
 * @date : 2020/1/15 16:08
 */
@Getter
@AllArgsConstructor
public enum EnumResponseMessageType {

    // 响应消息类型
    TEXT("text", "文本"),
    IMAGE("image", "图片"),
    VOICE("voice", "语音"),
    VIDEO("video", "视频"),
    MUSIC("music", "音乐"),
    NEWS("news", "图文");

    /**
     * 消息类型
     */
    private String type;
    /**
     * 消息类型对应描述
     */
    private String typeValue;

    public static EnumResponseMessageType getEnum(String type) {
        for (EnumResponseMessageType message : EnumResponseMessageType.values()) {
            if (message.type.equals(type)) {
                return message;
            }
        }
        return TEXT;
    }

}
