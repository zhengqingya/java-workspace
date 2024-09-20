package com.zhengqing.demo.netty.common.protocol;

import com.zhengqing.demo.netty.common.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p> 自定义通信协议 -- 编解码器 </p>
 *
 * @author zhengqingya
 * @description tips: 必须和 LengthFieldBasedFrameDecoder {@link ProtocolFrameDecoder } 一起使用，确保接收到的 ByteBuf 消息是完整的，即消息是无状态时，就可以加上多线程共享注解 {@link Sharable}，标识线程安全，就可以只创建一个实例
 * @date 2024/9/16 23:46
 */
@Slf4j
@ChannelHandler.Sharable
public class CustomMessageCodec extends MessageToMessageCodec<ByteBuf, Message> {
    @Override
    public void encode(ChannelHandlerContext ctx, Message msg, List<Object> outList) throws Exception {
        try {
            // 将Message对象转换为ByteBuf，并添加到out列表中
            ByteBuf out = ctx.alloc().buffer();
            // 1、魔数（4 字节）
            out.writeBytes(new byte[]{1, 2, 3, 4});
            // 2、版本号，可以支持协议的升级（1 字节）
            out.writeByte(1);
            // 3、序列化算法 java 0 , json 1 （1 字节）
            SerializerStrategy.Algorithm serializerAlgorithm = msg.getSerializerAlgorithm();
            out.writeByte(serializerAlgorithm.ordinal());
            // 4、指令类型（1 字节）
            out.writeByte(msg.getMessageType());
            // 5、请求序号（8 字节）
            out.writeLong(msg.getRequestId());
            // 对齐填充 （无实际意义，为了专业点，满2的n次方）
            out.writeByte(0xff);
            // 获取内容的字节数组
            byte[] bytes = serializerAlgorithm.serialize(msg);
            // 6、消息长度
            int _1MB = 1024 * 1024;
            if (bytes.length > _1MB) {
                throw new RuntimeException("Data is too long to send: " + bytes.length);
            }
            out.writeInt(bytes.length);
            // 7、消息正文
            out.writeBytes(bytes);
            outList.add(out);
        } catch (Exception e) {
            handleException(ctx, e);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> outList) throws Exception {
        try {
            if (in.readableBytes() < 16) {
                throw new RuntimeException("数据不足，无法正常拿到数据！");
            }

            int magicNum = in.readInt();
            byte version = in.readByte();
            byte serializerType = in.readByte();
            byte messageType = in.readByte();
            long requestId = in.readLong();
            in.readByte();

            int length = in.readInt();
            byte[] bytes = new byte[length];
            in.readBytes(bytes, 0, length);

            Class<? extends Message> messageClass = Message.getMessageClass(messageType);
            Message message = SerializerStrategy.Algorithm.values()[serializerType].deserialize(bytes, messageClass);
            log.debug("magicNum: {}, version: {}, serializerType: {}, messageType: {}, requestId: {}, length: {} message: {}", magicNum, version, serializerType, messageType, requestId, length, message);
            outList.add(message);
        } catch (Exception e) {
            handleException(ctx, e);
        }
    }

    /**
     * 异常处理方法
     * 日志记录：将异常信息记录到日志文件中。
     * 关闭连接：当发生严重错误时，可以关闭连接。
     * 重试机制：对于某些可恢复的错误，可以尝试重新编码或解码。
     * 通知客户端：向客户端发送错误信息。
     */
    private void handleException(ChannelHandlerContext ctx, Throwable e) {
        // 日志记录
        ctx.fireExceptionCaught(e);
        // 关闭连接
        ctx.close();
    }

}
