package com.zhengqing.common.netty.server;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.zhengqing.common.netty.constant.NettyRedisConstant;
import com.zhengqing.common.netty.enums.NettyMsgCmdType;
import com.zhengqing.common.netty.model.NettyMsgBase;
import com.zhengqing.common.netty.server.handler.AbstractMsgHandler;
import com.zhengqing.common.netty.server.handler.MsgHandlerStrategy;
import com.zhengqing.common.netty.util.NettyChannelAttrKeyUtil;
import com.zhengqing.common.netty.util.RedisUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> 消息处理器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/2/26 10:11
 */
@Slf4j
public class NettyMsgHandler extends SimpleChannelInboundHandler<NettyMsgBase> {

    /**
     * 读取到消息后进行处理
     *
     * @param ctx channel上下文
     * @param msg 发送消息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NettyMsgBase msg) {
        log.debug("【netty】 server接受消息：{}", JSONUtil.toJsonStr(msg));
        AbstractMsgHandler msgHandler = MsgHandlerStrategy.get(NettyMsgCmdType.getType(msg.getCmd()));
        msgHandler.handle(ctx, msgHandler.transform(msg.getData()));
    }

    /**
     * 出现异常的处理 打印报错日志
     *
     * @param ctx   channel上下文
     * @param cause 异常信息
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("【netty】 处理消息异常：", cause);
        // 关闭上下文
        // ctx.close();
    }

    /**
     * 监控浏览器上线
     *
     * @param ctx channel上下文
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        log.info("【netty】 上线连接：{} SocketAddress:{}", ctx.channel().id().asShortText(), ctx.channel().remoteAddress());
    }

    /**
     * 用于在 ChannelHandler 被从 ChannelPipeline 中移除时进行清理工作。
     *
     * @param ctx channel上下文
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        log.info("【netty】 断开连接：{} SocketAddress:{}", ctx.channel().id().asShortText(), ctx.channel().remoteAddress());
        Long userId = NettyChannelAttrKeyUtil.getAttr(ctx.channel(), NettyChannelAttrKeyUtil.USER_ID);
        Integer terminal = NettyChannelAttrKeyUtil.getAttr(ctx.channel(), NettyChannelAttrKeyUtil.TERMINAL);
        ChannelHandlerContext context = NettyUserCtxMap.getCtx(userId, terminal);
        // 判断一下，避免异地登录导致的误删
        if (context != null && ctx.channel().id().equals(context.channel().id())) {
            // 移除channel
            NettyUserCtxMap.remove(userId, terminal);
            // 用户下线
            RedisUtil.delete(StrUtil.format("{}:{}:{}", NettyRedisConstant.USER_RE_SERVER_ID, userId, terminal));
            log.info("【netty】 断开连接, userId:{}, 终端类型:{}", userId, terminal);
        }
    }

    /**
     * 用于处理用户触发的事件
     *
     * @param ctx channel上下文
     * @param evt 触发的事件
     * @throws Exception 异常
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                // 在规定时间内没有收到客户端的上行数据, 主动断开连接
                Long userId = NettyChannelAttrKeyUtil.getAttr(ctx.channel(), NettyChannelAttrKeyUtil.USER_ID);
                Integer terminal = NettyChannelAttrKeyUtil.getAttr(ctx.channel(), NettyChannelAttrKeyUtil.TERMINAL);
                ctx.channel().close();
                log.info("【netty】心跳超时断开连接，用户id:{}，终端类型:{} ", userId, terminal);
            }
        } else if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            // 监听握手完成事件
            WebSocketServerProtocolHandler.HandshakeComplete handshake = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            HttpHeaders headers = handshake.requestHeaders();
            log.debug("【netty】 ws请求头：");
            for (String name : headers.names()) {
                System.err.println(name + ": " + headers.get(name));
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}