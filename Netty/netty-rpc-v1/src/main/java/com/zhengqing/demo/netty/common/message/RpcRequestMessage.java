package com.zhengqing.demo.netty.common.message;

import com.zhengqing.demo.netty.common.protocol.SerializerStrategy;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p> 请求模型 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/9/16 23:48
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class RpcRequestMessage extends Message implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 调用的接口全限定名，服务端根据它找到实现
     */
    private String interfaceName;
    /**
     * 调用接口中的方法名
     */
    private String methodName;
    /**
     * 方法返回类型
     */
    private Class<?> returnType;
    /**
     * 方法参数类型数组
     */
    private Class<?>[] parameterTypes;
    /**
     * 方法参数值数组
     */
    private Object[] parameterValues;

    public RpcRequestMessage(long requestId, SerializerStrategy.Algorithm serializerAlgorithm, String interfaceName, String methodName, Class<?> returnType, Class[] parameterTypes, Object[] parameterValues) {
        super.setRequestId(requestId);
        super.setSerializerAlgorithm(serializerAlgorithm);
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        this.parameterValues = parameterValues;
    }

    @Override
    public int getMessageType() {
        return RPC_MESSAGE_TYPE_REQUEST;
    }

}
