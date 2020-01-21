package com.zhengqing.demo.modules.weixin.enumeration;


import lombok.AllArgsConstructor;

/**
 * <p> 事件类型枚举类 </p>
 *
 * @author : zhengqing
 * @description : 可参考微信开放文档定义：https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_event_pushes.html
 * @date : 2020/1/15 16:08
 */
@AllArgsConstructor
public enum EnumEventMessageType {

    // 事件类型
    SUBSCRIBE("subscribe", "订阅"),
    UNSUBSCRIBE("unsubscribe", "取消订阅"),
    CLICK("CLICK", "自定义菜单点击事件"),
    SCAN("SCAN", "用户已关注时的事件推送");

    /**
     * 事件类型
     */
    private String type;
    /**
     * 事件类型对应描述
     */
    private String typeValue;

    public static EnumEventMessageType getEnum(String type) {
        for (EnumEventMessageType message : EnumEventMessageType.values()) {
            if (message.type.equals(type)) {
                return message;
            }
        }
        return SUBSCRIBE;
    }

}
