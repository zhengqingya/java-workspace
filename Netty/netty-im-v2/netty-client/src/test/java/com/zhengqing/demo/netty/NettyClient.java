package com.zhengqing.demo.netty;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import com.zhengqing.common.netty.enums.NettyMsgCmdType;
import com.zhengqing.common.netty.model.NettyJwtUser;
import com.zhengqing.common.netty.model.NettyLogin;
import com.zhengqing.common.netty.model.NettyMsgBase;
import com.zhengqing.common.netty.util.JwtUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LoggingSystem;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * <p> 客户端 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/9/16 23:47
 */
@Slf4j
public class NettyClient {

    @SneakyThrows
    public static void main(String[] args) {
        LoggingSystem.get(LoggingSystem.class.getClassLoader()).setLogLevel("io.netty", org.springframework.boot.logging.LogLevel.INFO);
        URI WEB_SOCKET_URL = new URI("ws://127.0.0.1:10081/im");
        Channel channel = getChannel(WEB_SOCKET_URL);
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(
                NettyMsgBase.<NettyLogin>builder().cmd(NettyMsgCmdType.LOGIN.getType()).data(
                        NettyLogin.builder().accessToken(
                                JwtUtil.sign(
                                        NettyJwtUser.builder().userId(1L).terminal(0).build(),
                                        60000, "netty-im")
                        ).build()
                ).build()
        )));
        channel.eventLoop().schedule(() -> {
            log.debug("定时3秒发送一个心跳包");
            channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(
                    NettyMsgBase.<String>builder().cmd(NettyMsgCmdType.HEART_BEAT.getType()).data("ping").build()
            )));
        }, 3, TimeUnit.SECONDS);

        ThreadUtil.sleep(30, TimeUnit.SECONDS);
        log.info("终止程序");
        System.exit(0);
    }

    @SneakyThrows
    private static Channel getChannel(URI WEB_SOCKET_URL) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .group(group)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

                        // 用来判断是不是 读空闲时间过长，或 写空闲时间过长
                        // 3s 内如果没有向服务器写数据，会触发一个 IdleState#WRITER_IDLE 事件
                        ch.pipeline().addLast(new IdleStateHandler(0, 3, 0, TimeUnit.SECONDS));
                        // ChannelDuplexHandler 可以同时作为入站和出站处理器
                        ch.pipeline().addLast(new ChannelDuplexHandler() {
                            // 用来触发特殊事件
                            @Override
                            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                IdleStateEvent event = (IdleStateEvent) evt;
                                // 触发了写空闲事件
                                if (event.state() == IdleState.WRITER_IDLE) {
                                    log.debug("3s 没有写数据了，发送一个心跳包");
                                    ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(
                                            NettyMsgBase.<String>builder().cmd(NettyMsgCmdType.HEART_BEAT.getType()).data("ping").build()
                                    )));
                                }
                            }
                        });

                        ch.pipeline().addLast(
                                new HttpClientCodec(),
                                new ChunkedWriteHandler(),
                                new HttpObjectAggregator(64 * 1024),
                                new WebSocketClientProtocolHandler(WebSocketClientHandshakerFactory
                                        .newHandshaker(WEB_SOCKET_URL, WebSocketVersion.V13, null, false, new DefaultHttpHeaders())
                                )

                        );
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<TextWebSocketFrame>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
                                log.debug("[netty-client] 接收消息：{}", msg.text());
                            }

                            @Override
                            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                if (WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE.equals(evt)) {
                                    log.info("[netty-client] 握手完成:{}", ctx.channel().id().asShortText());
                                }
                                super.userEventTriggered(ctx, evt);
                            }
                        });
                    }
                });
        try {
            Channel channel = bootstrap.connect(WEB_SOCKET_URL.getHost(), WEB_SOCKET_URL.getPort()).sync().channel();
            channel.closeFuture().addListener(future -> {
                group.shutdownGracefully();
            });
            return channel;
        } catch (Exception e) {
            log.error("client error", e);
            throw new Exception(e);
        }
    }

}
