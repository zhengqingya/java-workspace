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

    public static final int LoginRequestMessage = 0;
    public static final int LoginResponseMessage = 1;
    public static final int ChatRequestMessage = 2;
    public static final int ChatResponseMessage = 3;
    public static final int GroupCreateRequestMessage = 4;
    public static final int GroupCreateResponseMessage = 5;
    public static final int GroupJoinRequestMessage = 6;
    public static final int GroupJoinResponseMessage = 7;
    public static final int GroupQuitRequestMessage = 8;
    public static final int GroupQuitResponseMessage = 9;
    public static final int GroupChatRequestMessage = 10;
    public static final int GroupChatResponseMessage = 11;
    public static final int GroupMembersRequestMessage = 12;
    public static final int GroupMembersResponseMessage = 13;
    public static final int PingMessage = 14;
    public static final int PongMessage = 15;

    private static final Map<Integer, Class<? extends Message>> MESSAGE_CLASS_MAP = new HashMap<>();

    static {
        MESSAGE_CLASS_MAP.put(LoginRequestMessage, LoginRequestMessage.class);
        MESSAGE_CLASS_MAP.put(LoginResponseMessage, LoginResponseMessage.class);
        MESSAGE_CLASS_MAP.put(ChatRequestMessage, ChatRequestMessage.class);
        MESSAGE_CLASS_MAP.put(ChatResponseMessage, ChatResponseMessage.class);
        MESSAGE_CLASS_MAP.put(GroupCreateRequestMessage, GroupCreateRequestMessage.class);
        MESSAGE_CLASS_MAP.put(GroupCreateResponseMessage, GroupCreateResponseMessage.class);
        MESSAGE_CLASS_MAP.put(GroupJoinRequestMessage, GroupJoinRequestMessage.class);
        MESSAGE_CLASS_MAP.put(GroupJoinResponseMessage, GroupJoinResponseMessage.class);
        MESSAGE_CLASS_MAP.put(GroupQuitRequestMessage, GroupQuitRequestMessage.class);
        MESSAGE_CLASS_MAP.put(GroupQuitResponseMessage, GroupQuitResponseMessage.class);
        MESSAGE_CLASS_MAP.put(GroupChatRequestMessage, GroupChatRequestMessage.class);
        MESSAGE_CLASS_MAP.put(GroupChatResponseMessage, GroupChatResponseMessage.class);
        MESSAGE_CLASS_MAP.put(GroupMembersRequestMessage, GroupMembersRequestMessage.class);
        MESSAGE_CLASS_MAP.put(GroupMembersResponseMessage, GroupMembersResponseMessage.class);
        MESSAGE_CLASS_MAP.put(PingMessage, PingMessage.class);
        MESSAGE_CLASS_MAP.put(PongMessage, PongMessage.class);
    }

    public static Class<? extends Message> getMessageClass(int messageType) {
        return MESSAGE_CLASS_MAP.get(messageType);
    }

    private long requestId;
    private int messageType;
    private SerializerStrategy.Algorithm serializerAlgorithm = SerializerStrategy.Algorithm.Json;

    public abstract int getMessageType();

}
