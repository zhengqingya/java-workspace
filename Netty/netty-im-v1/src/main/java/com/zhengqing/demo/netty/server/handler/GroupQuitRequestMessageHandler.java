package com.zhengqing.demo.netty.server.handler;

import com.zhengqing.demo.netty.common.message.GroupJoinResponseMessage;
import com.zhengqing.demo.netty.common.message.GroupQuitRequestMessage;
import com.zhengqing.demo.netty.common.session.Group;
import com.zhengqing.demo.netty.common.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class GroupQuitRequestMessageHandler extends SimpleChannelInboundHandler<GroupQuitRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupQuitRequestMessage msg) throws Exception {
        Group group = GroupSessionFactory.getGroupSession().removeMember(msg.getGroupName(), msg.getUsername());
        if (group != null) {
            ctx.writeAndFlush(new GroupJoinResponseMessage(true, "已退出群" + msg.getGroupName()));
        } else {
            ctx.writeAndFlush(new GroupJoinResponseMessage(false, msg.getGroupName() + "群不存在"));
        }
    }
}
