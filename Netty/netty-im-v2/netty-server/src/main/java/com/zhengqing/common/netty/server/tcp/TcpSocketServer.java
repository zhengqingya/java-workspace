package com.zhengqing.common.netty.server.tcp;

import com.zhengqing.common.netty.server.NettyMsgHandler;
import com.zhengqing.common.netty.server.NettyServerStrategy;
import com.zhengqing.common.netty.server.tcp.endecoder.TcpMsgDecoder;
import com.zhengqing.common.netty.server.tcp.endecoder.TcpMsgEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * <p> TCP 服务器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/2/23 11:52
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "netty.tcp-socket.enable", havingValue = "true", matchIfMissing = false)
public class TcpSocketServer implements NettyServerStrategy {

    private volatile boolean isReady = false;

    @Value("${netty.tcp-socket.port}")
    private int port;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;

    @Override
    public boolean isReady() {
        return this.isReady;
    }

    @Override
    public void start() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        this.bossGroup = new NioEventLoopGroup();
        this.workGroup = new NioEventLoopGroup();
        serverBootstrap
                // 设置主从线程模型
                .group(this.bossGroup, this.workGroup)
                // NIO 通信类型
                .channel(NioServerSocketChannel.class)
                // serverBootstrap 还可以设置TCP参数，根据需要可以分别设置主线程池和从线程池参数，来优化服务端性能。
                // 其中主线程池使用option方法来设置，从线程池使用childOption方法设置。
                // backlog表示主线程池中在套接口排队的最大数量，队列由未连接队列（三次握手未完成的）和已连接队列
                .option(ChannelOption.SO_BACKLOG, 128)
                // 表示连接保活，相当于心跳机制，默认为7200s
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // 设置ChannelPipeline，也就是业务职责链，由处理的Handler串联而成，由从线程池处理
                .childHandler(new ChannelInitializer<Channel>() {
                    // 添加处理的Handler，通常包括消息编解码、业务处理，也可以是日志、权限、过滤等
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        // 20秒客户端没有向服务器发送心跳则关闭连接
                        pipeline.addLast(new IdleStateHandler(20, 0, 0, TimeUnit.SECONDS));
                        pipeline.addLast("encode", new TcpMsgEncoder());
                        pipeline.addLast("decode", new TcpMsgDecoder());
                        pipeline.addLast("handler", new NettyMsgHandler());
                    }
                });

        try {
            // 绑定端口，启动select线程，轮询监听channel事件，监听到事件之后就会交给从线程池处理
            Channel channel = serverBootstrap.bind(this.port).sync().channel();
            // 就绪标志
            this.isReady = true;
            log.info("【netty】 tcp-server 初始化完成,端口：{}", this.port);
            // 等待服务端口关闭
            //channel.closeFuture().sync();
        } catch (Exception e) {
            log.info("【netty】 tcp-server 初始化异常：", e);
        }
    }

    @Override
    public void stop() {
        this.shutdownGracefully(this.bossGroup);
        this.shutdownGracefully(this.workGroup);
        this.isReady = false;
        log.info("【netty】 tcp-server server 停止");
    }

    public void shutdownGracefully(EventLoopGroup eventLoopGroup) {
        if (eventLoopGroup != null && !eventLoopGroup.isShuttingDown() && !eventLoopGroup.isShutdown()) {
            eventLoopGroup.shutdownGracefully();
        }
    }

}
