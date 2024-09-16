package com.zhengqing.demo.netty.common.message;

import com.zhengqing.demo.netty.common.protocol.SerializerStrategy;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p> 消息模型 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/9/16 23:48
 */
@Getter
@Setter
public abstract class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int RPC_MESSAGE_TYPE_REQUEST = 101;
    public static final int RPC_MESSAGE_TYPE_RESPONSE = 102;

    private static final Map<Integer, Class<? extends Message>> MESSAGE_CLASS_MAP = new HashMap<>();

    static {
        MESSAGE_CLASS_MAP.put(RPC_MESSAGE_TYPE_REQUEST, RpcRequestMessage.class);
        MESSAGE_CLASS_MAP.put(RPC_MESSAGE_TYPE_RESPONSE, RpcResponseMessage.class);
    }

    public static Class<? extends Message> getMessageClass(int messageType) {
        return MESSAGE_CLASS_MAP.get(messageType);
    }

    private long requestId;
    private int messageType;
    private SerializerStrategy.Algorithm serializerAlgorithm;

    public abstract int getMessageType();

}
