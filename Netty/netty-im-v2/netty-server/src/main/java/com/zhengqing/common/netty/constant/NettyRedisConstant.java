package com.zhengqing.common.netty.constant;

/**
 * <p> 全局常用变量 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/2/26 11:46
 */
public interface NettyRedisConstant {

    /**
     * 心跳超时时间 单位：秒
     */
    long HEARTBEAT_TIMEOUT_SECOND = 20;

    /**
     * 服务端最大id,从0开始递增
     */
    String MAX_SERVER_ID = "im:max_server_id";

    /**
     * 用户ID所连接的服务端ID
     */
    String USER_RE_SERVER_ID = "im:user:server_id";

}
