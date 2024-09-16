package com.zhengqing.demo.netty.common.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * <p> 帧解码器配置 </p>
 *
 * @author zhengqingya
 * @description {@link LengthFieldBasedFrameDecoder} 是一个用于处理基于长度字段的数据帧解码的类。
 * 通常在实现网络通信协议时，如果数据是以固定长度字段来标识后续数据包长度的方式传输，那么这个类就非常有用。
 * @date 2024/9/16 23:45
 */
public class ProtocolFrameDecoder extends LengthFieldBasedFrameDecoder {

    public ProtocolFrameDecoder() {
        /**
         * maxFrameLength: 数据帧的最大长度。这是为了防止内存溢出攻击或错误的数据导致无限制地分配内存。
         * lengthFieldOffset: 长度字段在数据帧中的起始位置（偏移量）。这有助于确定从哪里开始读取表示数据长度的信息。
         * lengthFieldLength: 长度字段本身的字节数。例如，如果长度字段是一个32位整数，则此值为4。
         * lengthAdjustment: 对读取到的长度字段进行调整的值。有时候实际的有效载荷长度可能需要加上或减去某个固定的数值。
         * initialBytesToStrip: 在有效载荷前忽略的字节数。有时候数据帧开始的部分并不是有效载荷的一部分，这部分会被忽略。 （编解码器decode的时候，自己处理了，所以这里为0，无需去掉前面的部分数据）
         */
        this(1024, 16, 4, 0, 0);
    }

    public ProtocolFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}
