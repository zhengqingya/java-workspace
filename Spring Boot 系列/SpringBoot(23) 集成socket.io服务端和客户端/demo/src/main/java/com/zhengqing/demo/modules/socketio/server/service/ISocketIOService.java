package com.zhengqing.demo.modules.socketio.server.service;


/**
 * <p> socket.io服务类 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/2/7 20:31
 */
public interface ISocketIOService {

    /**
     * 启动服务
     */
    void start();

    /**
     * 停止服务
     */
    void stop();

    /**
     * 推送信息给指定客户端
     *
     * @param userId:     客户端唯一标识
     * @param msgContent: 消息内容
     */
    void pushMessageToUser(String userId, String msgContent);
}
