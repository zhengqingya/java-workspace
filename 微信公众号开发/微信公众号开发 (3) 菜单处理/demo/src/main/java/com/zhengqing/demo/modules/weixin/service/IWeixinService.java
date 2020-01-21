package com.zhengqing.demo.modules.weixin.service;


import com.zhengqing.demo.modules.weixin.model.AccessTokenVO;

/**
 * <p> 微信公众号 - 服务类 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/16 16:13
 */
public interface IWeixinService {

    /**
     * 根据AppID和AppSecret获取access_token
     *
     * @param appId:
     * @param appSecret:
     * @return: com.zhengqing.demo.modules.weixin.model.AccessTokenVO
     */
    AccessTokenVO getAccessToken(String appId, String appSecret);

}
