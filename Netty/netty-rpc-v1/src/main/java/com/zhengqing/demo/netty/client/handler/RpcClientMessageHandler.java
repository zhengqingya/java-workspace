package com.zhengqing.demo.netty.client.handler;

import com.zhengqing.demo.netty.common.message.RpcResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> 客户端消息处理器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/9/16 23:47
 */
@Slf4j
@ChannelHandler.Sharable
public class RpcClientMessageHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {

    public static final Map<Object, Promise<Object>> SEQUENCE_ID_PROMISE = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponseMessage msg) throws Exception {
        log.debug("[netty-client] 接收消息：{}", msg);
        Promise<Object> promise = SEQUENCE_ID_PROMISE.remove(msg.getRequestId());
        if (promise == null) {
            return;
        }
        Exception error = msg.getError();
        if (error != null) {
            promise.setFailure(error);
        } else {
            promise.setSuccess(msg.getResult());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("netty client error: ", cause);
        ctx.close();
    }

}
