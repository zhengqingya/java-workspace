package com.zhengqing.demo.modules.weixin.service;


import com.zhengqing.demo.modules.weixin.model.AccessTokenVO;
import com.zhengqing.demo.modules.weixin.model.WeixinResponseResult;
import com.zhengqing.demo.modules.weixin.model.WeixinUserInfoVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    /**
     * 通过`code`换取网页授权`access_token`和`openid`
     *
     * @param request
     * @param response
     * @throws IOException
     */
    void getOpenId(HttpServletRequest request, HttpServletResponse response);

    /**
     * 获取微信用户基本信息
     *
     * @param openId:
     * @return: com.zhengqing.demo.modules.weixin.model.WeixinUserInfo
     */
    WeixinUserInfoVO getUserInfo(String openId, String accessToken);

    /**
     * 检验授权凭证（access_token）是否有效
     *
     * @param openId
     * @param accessToken:
     * @return: com.zhengqing.demo.modules.weixin.model.WeixinResponseResult
     */
    WeixinResponseResult checkAccessToken(String openId, String accessToken);

    /**
     * 刷新access_token（如果需要） 【由于access_token拥有较短的有效期，当access_token超时后，可以使用refresh_token进行刷新，refresh_token有效期为30天，当refresh_token失效之后，需要用户重新授权。】
     *
     * @param appId
     * @param refreshToken:
     * @return: java.lang.String
     */
    String refreshAccessToken(String appId, String refreshToken);
}
