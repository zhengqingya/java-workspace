package com.zhengqing.common.netty.server.handler;

import cn.hutool.json.JSONUtil;
import com.zhengqing.common.netty.enums.NettyMsgCmdType;
import com.zhengqing.common.netty.model.NettyChat;
import com.zhengqing.common.netty.model.NettyChatUserBO;
import com.zhengqing.common.netty.model.NettyMsgBase;
import com.zhengqing.common.netty.server.NettyUserCtxMap;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p> 私聊消息 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/2/27 17:06
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PrivateMsgHandler extends AbstractMsgHandler<NettyChat> {

    @Override
    NettyMsgCmdType getMsgCmdEnum() {
        return NettyMsgCmdType.PRIVATE_MESSAGE;
    }

    @Override
    public void handle(ChannelHandlerContext ctx, NettyChat nettyChat) {
        NettyChatUserBO sender = nettyChat.getSender();
        NettyChatUserBO receiver = nettyChat.getReceiverList().get(0);
        Long userId = sender.getUserId();
        Long receiverUserId = receiver.getUserId();
        Object chatData = nettyChat.getData();
        log.info("【netty】接收私聊消息，发送者:{}，接收者:{}，内容:{}", userId, receiverUserId, JSONUtil.toJsonStr(chatData));

        ChannelHandlerContext channelCtx = NettyUserCtxMap.getCtx(receiverUserId, receiver.getTerminal());
        if (channelCtx != null) {
            // 推送消息给用户
            channelCtx.channel().writeAndFlush(NettyMsgBase.builder().cmd(NettyMsgCmdType.PRIVATE_MESSAGE.getType()).data(chatData).build());
        } else {
            log.error("【netty】私聊未找到channel，发送者:{}，接收者:{}，内容:{}", userId, receiverUserId, JSONUtil.toJsonStr(chatData));
        }
    }

    @Override
    public NettyChat transform(Object data) {
        return super.transform(data, this.bodyClass);
    }

}
