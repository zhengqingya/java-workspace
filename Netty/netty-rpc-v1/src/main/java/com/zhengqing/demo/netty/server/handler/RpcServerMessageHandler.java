package com.zhengqing.demo.netty.server.handler;

import com.zhengqing.demo.netty.common.message.RpcRequestMessage;
import com.zhengqing.demo.netty.common.message.RpcResponseMessage;
import com.zhengqing.demo.service.TestService;
import com.zhengqing.demo.service.impl.TestServiceImpl;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * <p> 服务端消息处理器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/9/16 23:49
 */
@Slf4j
@ChannelHandler.Sharable
public class RpcServerMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage msg) {
        log.debug("[netty-server] 接收消息：{}", msg);
        RpcResponseMessage response = new RpcResponseMessage();
        response.setSerializerAlgorithm(msg.getSerializerAlgorithm());
        response.setRequestId(msg.getRequestId());
        try {
            TestService service = new TestServiceImpl();
            Method method = service.getClass().getMethod(msg.getMethodName(), msg.getParameterTypes());
            Object invoke = method.invoke(service, msg.getParameterValues());
            response.setResult(invoke);
        } catch (Exception e) {
            log.error("rpc调用失败: ", e);
            response.setError(new Exception("rpc调用失败: " + e.getCause().getMessage()));
        }
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("netty server error: ", cause);
        ctx.close();
    }

}
