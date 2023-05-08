package com.zhengqing.demo.modules.chat.client;

import com.zhengqing.demo.modules.chat.client.event.ChatBaseEventImpl;
import com.zhengqing.demo.modules.chat.client.event.ChatTransDataEventImpl;
import com.zhengqing.demo.modules.chat.client.event.MessageQoSEventImpl;
import net.openmob.mobileimsdk.java.ClientCoreSDK;
import net.openmob.mobileimsdk.java.conf.ConfigEntity;

/**
 * <p> MobileIMSDK初始化 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/2/6 12:32
 */
public class IMClientManager {
    private static IMClientManager instance = null;

    /**
     * MobileIMSDK是否已被初始化. true表示已初化完成，否则未初始化.
     */
    private boolean init = false;

    public static IMClientManager getInstance() {
        if (instance == null) {
            instance = new IMClientManager();
        }
        return instance;
    }

    private IMClientManager() {
        initMobileIMSDK();
    }

    public void initMobileIMSDK() {
        if (!init) {
            // 设置AppKey
            ConfigEntity.appKey = "5418023dfd98c579b6001741";

            // 设置服务器ip和服务器端口
            ConfigEntity.serverIP = "127.0.0.1";
            ConfigEntity.serverUDPPort = 7901;

            // MobileIMSDK核心IM框架的敏感度模式设置
//			ConfigEntity.setSenseMode(SenseMode.MODE_10S);

            // 开启/关闭DEBUG信息输出
            ClientCoreSDK.DEBUG = false;

            // 设置事件回调
            ClientCoreSDK.getInstance().setChatBaseEvent(new ChatBaseEventImpl());
            ClientCoreSDK.getInstance().setChatTransDataEvent(new ChatTransDataEventImpl());
            ClientCoreSDK.getInstance().setMessageQoSEvent(new MessageQoSEventImpl());

            init = true;
        }
    }

}
