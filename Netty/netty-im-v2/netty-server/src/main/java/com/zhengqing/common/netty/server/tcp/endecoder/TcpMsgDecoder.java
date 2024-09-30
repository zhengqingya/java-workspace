package com.zhengqing.common.netty.server.tcp.endecoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhengqing.common.netty.model.NettyMsgBase;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p> 消息解码器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/2/26 11:11
 */
@Slf4j
public class TcpMsgDecoder extends ReplayingDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < 4) {
            return;
        }
        // 获取到包的长度
        long length = byteBuf.readLong();
        ByteBuf contentBuf = byteBuf.readBytes((int) length);
        String content = contentBuf.toString(CharsetUtil.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        NettyMsgBase sendInfo = objectMapper.readValue(content, NettyMsgBase.class);
        list.add(sendInfo);
    }

}
