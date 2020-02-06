package com.zhengqing.demo.modules.chat.client.service;

/**
 * <p> 聊天服务类$ </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/2/5$ 17:00$
 */
public interface IChatService {

    /**
     * 登录连接IM服务器请求
     *
     * @param username: 用户名
     * @param password: 密码
     * @return: void
     */
    void loginConnect(String username, String password);

    /**
     * 发送消息
     *
     * @param friendId: 接收消息者id
     * @param msg:      消息内容
     * @return: void
     */
    void sendMsg(String friendId, String msg);

}
