package com.zhengqing.demo.modules.chat.client.event;


import lombok.extern.slf4j.Slf4j;
import net.openmob.mobileimsdk.java.event.ChatTransDataEvent;

/**
 * <p> 接收消息事件 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/2/6 12:51
 */
@Slf4j
public class ChatTransDataEventImpl implements ChatTransDataEvent {

    @Override
    public void onTransBuffer(String fingerPrintOfProtocal, String userid, String dataContent, int typeu) {
        log.debug("[typeu=" + typeu + "]收到来自用户" + userid + "的消息:" + dataContent);
    }

    @Override
    public void onErrorResponse(int errorCode, String errorMsg) {
        log.debug("收到服务端错误消息，errorCode=" + errorCode + ", errorMsg=" + errorMsg);
    }

}
