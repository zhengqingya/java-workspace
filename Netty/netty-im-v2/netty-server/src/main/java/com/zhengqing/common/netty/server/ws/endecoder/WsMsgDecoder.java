package com.zhengqing.common.netty.server.ws.endecoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhengqing.common.netty.model.NettyMsgBase;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

/**
 * <p> 消息解码器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/2/26 11:11
 */
public class WsMsgDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame, List<Object> list) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        NettyMsgBase sendInfo = objectMapper.readValue(textWebSocketFrame.text(), NettyMsgBase.class);
        list.add(sendInfo);
    }

}
