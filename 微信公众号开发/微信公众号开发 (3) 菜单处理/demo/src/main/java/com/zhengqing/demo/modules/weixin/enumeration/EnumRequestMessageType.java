package com.zhengqing.demo.modules.weixin.enumeration;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p> 请求消息类型枚举类 </p>
 *
 * @author : zhengqing
 * @description : 可参考微信开放文档定义：https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html
 * @date : 2020/1/15 16:08
 */
@Getter
@AllArgsConstructor
public enum EnumRequestMessageType {

    // 请求消息类型
    TEXT("text", "文本"),
    IMAGE("image", "图片"),
    VOICE("voice", "语音"),
    VIDEO("video", "视频"),
    SHORTVIDEO("shortvideo", "小视频"),
    LOCATION("location", "地理位置"),
    LINK("link", "链接"),

    // 事件推送
    EVENT("event", "推送");

    /**
     * 消息类型
     */
    private String type;
    /**
     * 消息类型对应描述
     */
    private String typeValue;

    public static EnumRequestMessageType getEnum(String type) {
        for (EnumRequestMessageType message : EnumRequestMessageType.values()) {
            if (message.type.equals(type)) {
                return message;
            }
        }
        return TEXT;
    }

}
