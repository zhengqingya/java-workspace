package com.zhengqing.demo.netty.server.handler;

import com.zhengqing.demo.netty.common.message.LoginRequestMessage;
import com.zhengqing.demo.netty.common.message.LoginResponseMessage;
import com.zhengqing.demo.netty.common.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        String username = msg.getUsername();
        String password = msg.getPassword();
        SessionFactory.getSession().bind(ctx.channel(), username);
        ctx.writeAndFlush(new LoginResponseMessage(true, username + "登录成功"));
    }
}
