package com.zhengqing.common.netty.server.ws.endecoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhengqing.common.netty.model.NettyMsgBase;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

/**
 * <p> 消息编码器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/2/26 11:11
 */
public class WsMsgEncoder extends MessageToMessageEncoder<NettyMsgBase> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, NettyMsgBase sendInfo, List<Object> list) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String text = objectMapper.writeValueAsString(sendInfo);
        TextWebSocketFrame frame = new TextWebSocketFrame(text);
        list.add(frame);
    }

}
