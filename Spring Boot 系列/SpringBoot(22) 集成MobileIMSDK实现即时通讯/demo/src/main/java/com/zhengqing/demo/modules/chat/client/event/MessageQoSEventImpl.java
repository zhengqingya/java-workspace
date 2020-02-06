package com.zhengqing.demo.modules.chat.client.event;


import lombok.extern.slf4j.Slf4j;
import net.openmob.mobileimsdk.java.event.MessageQoSEvent;
import net.openmob.mobileimsdk.server.protocal.Protocal;

import java.util.ArrayList;

/**
 * <p> 消息是否送达事件 </p>
 *
 * @author : zhengqing
 * @description : 由QoS机制通知上来的
 * @date : 2020/2/6 12:52
 */
@Slf4j
public class MessageQoSEventImpl implements MessageQoSEvent {

    @Override // 对方未成功接收消息的回调事件 lostMessages：存放消息内容
    public void messagesLost(ArrayList<Protocal> lostMessages) {
        log.debug("收到系统的未实时送达事件通知，当前共有" + lostMessages.size() + "个包QoS保证机制结束，判定为【无法实时送达】！");
    }

    @Override // 对方成功接收到消息的回调事件
    public void messagesBeReceived(String theFingerPrint) {
        if (theFingerPrint != null) {
            log.debug("收到对方已收到消息事件的通知，fp=" + theFingerPrint);
        }
    }

}
