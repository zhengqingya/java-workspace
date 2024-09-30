package com.zhengqing.common.netty.server.handler;

import com.zhengqing.common.netty.enums.NettyMsgCmdType;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> 注册消息处理器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/2/26 16:16
 */
public class MsgHandlerStrategy {
    private static final Map<NettyMsgCmdType, AbstractMsgHandler> STRATEGY_MAP = new HashMap<>();

    public static void register(NettyMsgCmdType nettyMsgCmdType, AbstractMsgHandler abstractMsgHandler) {
        STRATEGY_MAP.put(nettyMsgCmdType, abstractMsgHandler);
    }

    public static AbstractMsgHandler get(NettyMsgCmdType nettyMsgCmdType) {
        return STRATEGY_MAP.get(nettyMsgCmdType);
    }
}
