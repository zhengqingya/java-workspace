package com.zhengqing.demo.netty.server.handler;

import com.zhengqing.demo.netty.common.message.GroupJoinRequestMessage;
import com.zhengqing.demo.netty.common.message.GroupJoinResponseMessage;
import com.zhengqing.demo.netty.common.session.Group;
import com.zhengqing.demo.netty.common.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class GroupJoinRequestMessageHandler extends SimpleChannelInboundHandler<GroupJoinRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupJoinRequestMessage msg) throws Exception {
        Group group = GroupSessionFactory.getGroupSession().joinMember(msg.getGroupName(), msg.getUsername());
        if (group != null) {
            ctx.writeAndFlush(new GroupJoinResponseMessage(true, msg.getGroupName() + "群加入成功"));
        } else {
            ctx.writeAndFlush(new GroupJoinResponseMessage(false, msg.getGroupName() + "群不存在"));
        }
    }
}
