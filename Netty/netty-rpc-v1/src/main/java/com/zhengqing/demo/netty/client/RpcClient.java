package com.zhengqing.demo.netty.client;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.zhengqing.demo.netty.client.handler.RpcClientMessageHandler;
import com.zhengqing.demo.netty.common.message.RpcRequestMessage;
import com.zhengqing.demo.netty.common.protocol.CustomMessageCodec;
import com.zhengqing.demo.netty.common.protocol.ProtocolFrameDecoder;
import com.zhengqing.demo.netty.common.protocol.SerializerStrategy;
import com.zhengqing.demo.service.TestService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LoggingSystem;

import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * <p> 客户端 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/9/16 23:47
 */
@Slf4j
public class RpcClient {

    public static void main(String[] args) {
        LoggingSystem.get(LoggingSystem.class.getClassLoader()).setLogLevel("io.netty", org.springframework.boot.logging.LogLevel.INFO);

        TestService proxyService = getProxyService(TestService.class);
        String result = proxyService.hello("zq");
        System.out.println("调用结果：" + result);

        ThreadUtil.sleep(3, TimeUnit.SECONDS);
        log.info("终止程序");
        System.exit(0);
    }

    public static <T> T getProxyService(Class<T> serviceClass) {
        Object proxyInstance = Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[]{serviceClass},
                (proxy, method, args) -> {
                    // 1、封装请求消息
                    long requestId = IdUtil.getSnowflakeNextId();
                    RpcRequestMessage msg = new RpcRequestMessage(
                            requestId,
                            SerializerStrategy.Algorithm.Json,
                            serviceClass.getName(),
                            method.getName(),
                            method.getReturnType(),
                            method.getParameterTypes(),
                            args
                    );

                    // 2、发送消息
                    log.debug("netty client send msg: {}", JSONUtil.toJsonStr(msg));
                    getChannel().writeAndFlush(msg);

                    // 3、指定promise对象异步接收结果
                    DefaultPromise<Object> promise = new DefaultPromise<>(getChannel().eventLoop());
                    RpcClientMessageHandler.SEQUENCE_ID_PROMISE.put(requestId, promise);

                    // 4、等待结果
                    promise.await();
                    if (promise.isSuccess()) {
                        return promise.getNow();
                    } else {
                        throw new RuntimeException(promise.cause());
                    }
                });
        return (T) proxyInstance;
    }

    private static Channel channel = null;
    private static final Object LOCK = new Object();

    public static Channel getChannel() {
        if (channel != null) {
            return channel;
        }
        synchronized (LOCK) {
            if (channel != null) {
                return channel;
            }
            initChannel();
            return channel;
        }
    }

    private static void initChannel() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        CustomMessageCodec MESSAGE_CODEC = new CustomMessageCodec();
        RpcClientMessageHandler RPC_HANDLER = new RpcClientMessageHandler();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ProtocolFrameDecoder());
                        ch.pipeline().addLast(LOGGING_HANDLER);
                        ch.pipeline().addLast(MESSAGE_CODEC);
                        ch.pipeline().addLast(RPC_HANDLER);
                    }
                });
        try {
            channel = bootstrap.connect("localhost", 8080).sync().channel();
            channel.closeFuture().addListener(future -> {
                group.shutdownGracefully();
            });
        } catch (Exception e) {
            log.error("client error", e);
        }
    }

}
