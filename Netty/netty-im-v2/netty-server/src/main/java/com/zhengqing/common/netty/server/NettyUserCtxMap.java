package com.zhengqing.common.netty.server;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> 用户与channel上下文关系 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/2/26 15:50
 */
public class NettyUserCtxMap {

    private static Map<Long, Map<Integer, ChannelHandlerContext>> USER_CTX_MAP = new ConcurrentHashMap<>();

    public static void add(Long userId, Integer terminal, ChannelHandlerContext ctx) {
        USER_CTX_MAP.computeIfAbsent(userId, key -> new ConcurrentHashMap<>()).put(terminal, ctx);
    }

    public static void remove(Long userId, Integer terminal) {
        if (userId != null && terminal != null && USER_CTX_MAP.containsKey(userId)) {
            Map<Integer, ChannelHandlerContext> map = USER_CTX_MAP.get(userId);
            map.remove(terminal);
        }
    }

    public static ChannelHandlerContext getCtx(Long userId, Integer terminal) {
        if (userId != null && terminal != null && USER_CTX_MAP.containsKey(userId)) {
            Map<Integer, ChannelHandlerContext> map = USER_CTX_MAP.get(userId);
            if (map.containsKey(terminal)) {
                return map.get(terminal);
            }
        }
        return null;
    }

    public static Map<Integer, ChannelHandlerContext> getCtx(Long userId) {
        if (userId == null) {
            return null;
        }
        return USER_CTX_MAP.get(userId);
    }

}
