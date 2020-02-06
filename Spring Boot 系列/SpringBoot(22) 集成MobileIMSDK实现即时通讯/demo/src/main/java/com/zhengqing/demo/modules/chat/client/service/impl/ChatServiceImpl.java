package com.zhengqing.demo.modules.chat.client.service.impl;

import com.zhengqing.demo.modules.chat.client.IMClientManager;
import com.zhengqing.demo.modules.chat.client.service.IChatService;
import lombok.extern.slf4j.Slf4j;
import net.openmob.mobileimsdk.java.core.LocalUDPDataSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p> 聊天服务实现类$ </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/2/5$ 17:00$
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ChatServiceImpl implements IChatService {

    @Override
    public void loginConnect(String username, String password) {
        // 确保MobileIMSDK被初始化哦（整个APP生生命周期中只需调用一次哦）
        // 提示：在不退出APP的情况下退出登陆后再重新登陆时，请确保调用本方法一次，不然会报code=203错误哦！
        IMClientManager.getInstance().initMobileIMSDK();

        // * 异步提交登陆名和密码
        new LocalUDPDataSender.SendLoginDataAsync(username, password) {
            /**
             * 登陆信息发送完成后将调用本方法（注意：此处仅是登陆信息发送完成，真正的登陆结果要在异步回调中处理哦）。
             * @param code 数据发送返回码，0 表示数据成功发出，否则是错误码
             */
            protected void fireAfterSendLogin(int code) {
                if (code == 0) {
                    log.debug("数据发送成功！");
                } else {
                    log.error("数据发送失败。错误码是：" + code);
                }
            }
        }.execute();
    }

    @Override
    public void sendMsg(String friendId, String msg) {
        // 发送消息（异步提升体验，你也可直接调用LocalUDPDataSender.send(..)方法发送）
        new LocalUDPDataSender.SendCommonDataAsync(msg, friendId) {
            @Override
            protected void onPostExecute(Integer code) {
                if (code == 0) {
                    log.debug("数据已成功发出！");
                } else {
                    log.error("数据发送失败。错误码是：" + code + "！");
                }
            }
        }.execute();
    }

}
