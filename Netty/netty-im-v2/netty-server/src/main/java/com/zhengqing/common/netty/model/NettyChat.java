package com.zhengqing.common.netty.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * <p> 私聊，群聊 消息实体类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/2/27 17:04
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NettyChat {

    /**
     * 发送方
     */
    private NettyChatUserBO sender;

    /**
     * 接收方
     */
    List<NettyChatUserBO> receiverList;

    /**
     * 推送消息体
     */
    private Object data;

    /**
     * 是否需要回调结果
     */
    private Boolean isCallbackResult;

    /**
     * 当前服务名（回调发送结果使用）
     */
    private String serviceName;

}


