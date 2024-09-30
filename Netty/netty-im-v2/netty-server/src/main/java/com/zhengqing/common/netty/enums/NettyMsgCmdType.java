package com.zhengqing.common.netty.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;

/**
 * <p> 消息类型 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/2/26 15:31
 */
@Getter
@AllArgsConstructor
public enum NettyMsgCmdType {

    /**
     * 登陆
     */
    LOGIN(0, "登陆"),
    /**
     * 心跳
     */
    HEART_BEAT(1, "心跳"),
    /**
     * 强制下线
     */
    FORCE_LOGOUT(2, "强制下线"),
    /**
     * 私聊消息
     */
    PRIVATE_MESSAGE(3, "私聊消息"),
    /**
     * 群发消息
     */
    GROUP_MESSAGE(4, "群发消息");

    /**
     * 类型
     */
    private final Integer type;

    /**
     * 描述
     */
    private final String desc;


    private static final List<NettyMsgCmdType> LIST = Arrays.asList(NettyMsgCmdType.values());

    @SneakyThrows
    public static NettyMsgCmdType getType(Integer type) {
        for (NettyMsgCmdType typeEnum : LIST) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum;
            }
        }
        throw new Exception("netty消息类型未知！");
    }


}

