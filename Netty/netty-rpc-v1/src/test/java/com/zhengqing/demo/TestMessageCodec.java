package com.zhengqing.demo;

import cn.hutool.core.util.IdUtil;
import com.zhengqing.demo.netty.common.message.Message;
import com.zhengqing.demo.netty.common.message.RpcRequestMessage;
import com.zhengqing.demo.netty.common.protocol.SerializerStrategy;
import com.zhengqing.demo.service.impl.TestServiceImpl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LoggingSystem;

import java.util.List;

/**
 * <p> 编解码测试 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/9/17 0:30
 */
public class TestMessageCodec {

    public static void main(String[] args) throws Exception {
        LoggingSystem.get(LoggingSystem.class.getClassLoader()).setLogLevel("io.netty", org.springframework.boot.logging.LogLevel.INFO);

        EmbeddedChannel channel = new EmbeddedChannel(
                new LoggingHandler(LogLevel.DEBUG),
                new LengthFieldBasedFrameDecoder(1024, 16, 4, 0, 0),
                new TestCustomMessageCodec()
        );
        RpcRequestMessage msg = new RpcRequestMessage(IdUtil.getSnowflakeNextId(),
                SerializerStrategy.Algorithm.Json,
                TestServiceImpl.class.getName(),
                "hello",
                String.class,
                new Class[]{String.class},
                new Object[]{"zhengqingya"});

        // 编码
        channel.writeOutbound(msg);

        // 解码
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        new TestCustomMessageCodec().encode(null, msg, buf);
        channel.writeInbound(buf);
    }


    @Slf4j
//    @ChannelHandler.Sharable
    public static class TestCustomMessageCodec extends ByteToMessageCodec<Message> {
        @Override
        public void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
            try {
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

}
