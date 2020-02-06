package com.zhengqing.demo.modules.chat.client.event;

import lombok.extern.slf4j.Slf4j;
import net.openmob.mobileimsdk.java.event.ChatBaseEvent;

/**
 * <p> 客户端与IM服务端连接事件 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/2/6 12:44
 */
@Slf4j
public class ChatBaseEventImpl implements ChatBaseEvent {

    @Override
    public void onLoginMessage(int dwErrorCode) {
        if (dwErrorCode == 0) {
            log.debug("IM服务器登录/连接成功！");
        } else {
            log.error("IM服务器登录/连接失败，错误代码：" + dwErrorCode);
        }
    }

    @Override
    public void onLinkCloseMessage(int dwErrorCode) {
        log.error("与IM服务器的网络连接出错关闭了，error：" + dwErrorCode);
    }

}
