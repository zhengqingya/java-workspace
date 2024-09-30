package com.zhengqing.common.netty.server.handler;

import cn.hutool.json.JSONUtil;
import com.zhengqing.common.netty.enums.NettyMsgCmdType;
import io.netty.channel.ChannelHandlerContext;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;

/**
 * <p> 消息处理抽象类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/2/26 16:11
 */
public abstract class AbstractMsgHandler<T> {

    /**
     * 消息实体类
     */
    protected Class<T> bodyClass;

    /**
     * 消息类型
     */
    abstract NettyMsgCmdType getMsgCmdEnum();

    @PostConstruct
    private void init() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.bodyClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
        MsgHandlerStrategy.register(this.getMsgCmdEnum(), this);
    }

    /**
     * 消息转换
     */
    public T transform(Object data) {
        return (T) data;
    }

    /**
     * 消息转换
     */
    public T transform(Object data, Class<T> clz) {
        return JSONUtil.toBean(JSONUtil.toJsonStr(data), clz);
    }

    /**
     * 消息处理
     */
    public void handle(ChannelHandlerContext ctx, T data) {
    }

    /**
     * 消息处理
     */
    public void handle(T data) {
    }


}
