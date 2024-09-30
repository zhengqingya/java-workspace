package com.zhengqing.common.netty.util;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * <p> AttributeKey </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/2/27 14:09
 */
public class NettyChannelAttrKeyUtil {

    public static AttributeKey<Long> USER_ID = AttributeKey.valueOf("USER_ID");
    public static AttributeKey<Integer> TERMINAL = AttributeKey.valueOf("TERMINAL_TYPE");
    public static AttributeKey<Long> HEARTBEAT_TIMES = AttributeKey.valueOf("HEARTBEAT_TIMES");

    public static <T> void setAttr(Channel channel, AttributeKey<T> attributeKey, T data) {
        channel.attr(attributeKey).set(data);
    }

    public static <T> T getAttr(Channel channel, AttributeKey<T> attributeKey) {
        return channel.attr(attributeKey).get();
    }

}
