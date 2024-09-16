package com.zhengqing.demo.netty.common.message;

import lombok.*;

import java.io.Serializable;

/**
 * <p> 响应模型 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/9/16 23:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RpcResponseMessage extends Message implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 返回值
     */
    private Object result;
    /**
     * 异常值
     */
    private Exception error;

    @Override
    public int getMessageType() {
        return RPC_MESSAGE_TYPE_RESPONSE;
    }
}
