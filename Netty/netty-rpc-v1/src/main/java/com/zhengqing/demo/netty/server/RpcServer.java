package com.zhengqing.demo.netty.server;

import com.zhengqing.demo.netty.common.protocol.CustomMessageCodec;
import com.zhengqing.demo.netty.common.protocol.ProtocolFrameDecoder;
import com.zhengqing.demo.netty.server.handler.RpcServerMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> 服务端 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/9/16 23:49
 */
@Slf4j
public class RpcServer {
    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        CustomMessageCodec MESSAGE_CODEC = new CustomMessageCodec();
        RpcServerMessageHandler RPC_HANDLER = new RpcServerMessageHandler();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(
                                    new ProtocolFrameDecoder(),
                                    LOGGING_HANDLER,
                                    MESSAGE_CODEC,
                                    RPC_HANDLER
                            );
                        }
                    });
            Channel channel = serverBootstrap.bind(8080).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("netty server error", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
