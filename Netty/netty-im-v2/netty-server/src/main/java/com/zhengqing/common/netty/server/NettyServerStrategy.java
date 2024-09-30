package com.zhengqing.common.netty.server;

/**
 * <p> netty 服务端 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/2/23 11:48
 */
public interface NettyServerStrategy {

    /**
     * 检查服务器是否已准备好
     *
     * @return 如果服务器已准备好，则返回 true，否则返回 false
     * @author zhengqingya
     * @date 2024/2/23 11:49
     */
    boolean isReady();

    /**
     * 启动服务器
     *
     * @return void
     * @author zhengqingya
     * @date 2024/2/23 11:49
     */
    void start();

    /**
     * 停止服务器
     *
     * @return void
     * @author zhengqingya
     * @date 2024/2/23 11:50
     */
    void stop();

}
