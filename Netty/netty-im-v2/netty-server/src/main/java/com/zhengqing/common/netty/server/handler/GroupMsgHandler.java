package com.zhengqing.common.netty.server.handler;

import com.zhengqing.common.netty.enums.NettyMsgCmdType;
import com.zhengqing.common.netty.model.NettyChat;
import com.zhengqing.common.netty.model.NettyChatUserBO;
import com.zhengqing.common.netty.model.NettyMsgBase;
import com.zhengqing.common.netty.server.NettyUserCtxMap;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p> 群聊 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/2/27 17:06
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GroupMsgHandler extends AbstractMsgHandler<NettyChat> {

    @Override
    NettyMsgCmdType getMsgCmdEnum() {
        return NettyMsgCmdType.GROUP_MESSAGE;
    }

    @Override
    public void handle(ChannelHandlerContext ctx, NettyChat nettyChat) {
        NettyChatUserBO sender = nettyChat.getSender();
        List<NettyChatUserBO> receiverList = nettyChat.getReceiverList();
        Long userId = sender.getUserId();
        Object chatData = nettyChat.getData();
        log.info("【netty】接收群聊消息，发送者:{}，接收用户数量:{}，内容:{}", userId, receiverList.size(), chatData);
        receiverList.parallelStream().forEach(item -> {
            ChannelHandlerContext channelCtx = NettyUserCtxMap.getCtx(item.getUserId(), item.getTerminal());
            if (channelCtx != null) {
                // 推送消息给用户
                channelCtx.channel().writeAndFlush(NettyMsgBase.builder().cmd(NettyMsgCmdType.GROUP_MESSAGE.getType()).data(chatData).build());
            } else {
                log.error("【netty】群聊未找到channel，发送者:{}，接收者:{}，内容:{}", userId, item.getUserId(), chatData);
            }
        });
    }

    @Override
    public NettyChat transform(Object data) {
        return super.transform(data, this.bodyClass);
    }

}
