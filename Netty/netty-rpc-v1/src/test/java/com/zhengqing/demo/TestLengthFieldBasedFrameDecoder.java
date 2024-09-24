package com.zhengqing.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * <p> 测试帧解码器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/9/24 12:25
 */
public class TestLengthFieldBasedFrameDecoder {
    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(
                /**
                 * maxFrameLength: 数据帧的最大长度。这是为了防止内存溢出攻击或错误的数据导致无限制地分配内存。
                 * lengthFieldOffset: 长度字段在数据帧中的起始位置（偏移量）。这有助于确定从哪里开始读取表示数据长度的信息。
                 * lengthFieldLength: 长度字段本身的字节数。例如，如果长度字段是一个32位整数，则此值为4。
                 * lengthAdjustment: 对读取到的长度字段进行调整的值。有时候实际的有效载荷长度可能需要加上或减去某个固定的数值。
                 * initialBytesToStrip: 在有效载荷前忽略的字节数。有时候数据帧开始的部分并不是有效载荷的一部分，这部分会被忽略。 （编解码器decode的时候，自己处理了，所以这里为0，无需去掉前面的部分数据）
                 */
                new LengthFieldBasedFrameDecoder(1024, 1, 4, 1, 6),
                new LoggingHandler(LogLevel.DEBUG)
        );

        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        send(buffer, "hello,world");
        send(buffer, "123");
        channel.writeInbound(buffer);
    }

    private static void send(ByteBuf buffer, String content) {
        buffer.writeByte(1); // （1个字节）

        byte[] bytes = content.getBytes(); // 实际内容
        int length = bytes.length;
        buffer.writeInt(length);  // 实际内容长度 （4个字节）

        buffer.writeByte(1); // （1个字节）

        buffer.writeBytes(bytes);
    }
}

