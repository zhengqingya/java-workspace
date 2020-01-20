package com.zhengqing.demo.modules.weixin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhengqing.demo.config.Constants;
import com.zhengqing.demo.modules.common.exception.MyException;
import com.zhengqing.demo.modules.weixin.model.AccessTokenVO;
import com.zhengqing.demo.modules.weixin.model.WeixinResponseResult;
import com.zhengqing.demo.modules.weixin.model.WeixinUserInfoVO;
import com.zhengqing.demo.modules.weixin.service.IWeixinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p> 微信公众号 - 服务实现类 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/16 16:15
 */
@Slf4j
@Service
public class WeixinServiceImpl implements IWeixinService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public AccessTokenVO getAccessToken(String appId, String appSecret) {
        AccessTokenVO accessTokenVO = restTemplate.getForObject(Constants.GET_ACCESS_TOKEN_URL.replace("APPID", appId).replace("APPSECRET", appSecret), AccessTokenVO.class);
        return accessTokenVO;
    }

    @Override
    public void getOpenId(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getParameter("code");
        String state = request.getParameter("state");

        log.debug("======================================= \n code值：" + code);

        String responseContent = restTemplate.getForObject(Constants.AUTH_GET_ACCESS_TOKEN_AND_OPENID
                .replace("APPID", Constants.APP_ID)
                .replace("SECRET", Constants.APP_SECRET)
                .replace("CODE", code), String.class);
        JSONObject result = JSON.parseObject(responseContent);
        String accessToken = result.getString("access_token");
        String openid = result.getString("openid");
        String refreshToken = result.getString("refresh_token");

        log.debug("======================================= \n access_token值：" + accessToken + "\n  openid值：" + openid);

        String redirectUrl = "http://m9adhq.natappfree.cc/api/weixin/basic/getUserInfo?openid=" + openid + "&access_token=" + accessToken;
        try {
            // 授权之后重定向到指定URL （这里是跳转到获取用户基本信息接口）
//            response.sendRedirect("http://m9adhq.natappfree.cc/swagger-ui.html");
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public WeixinUserInfoVO getUserInfo(String openId, String accessToken) {
        // ① 检验授权凭证（access_token）是否有效
        checkAccessToken(openId, accessToken);

        // ② 获取用户基本信息
        WeixinUserInfoVO weixinUserInfoVO = null;
        String responseContent = restTemplate.getForObject(Constants.AUTH_GET_USER_INFO
                .replace("ACCESS_TOKEN", accessToken)
                .replace("OPENID", openId), String.class);
        weixinUserInfoVO = JSON.parseObject(responseContent, WeixinUserInfoVO.class);
        return weixinUserInfoVO;
    }

    @Override
    public WeixinResponseResult checkAccessToken(String openId, String accessToken) {
        String responseResult = restTemplate.getForObject(Constants.AUTH_CHECK_ACCESS_TOKEN
                .replace("ACCESS_TOKEN", accessToken)
                .replace("OPENID", openId), String.class);
        WeixinResponseResult weixinResponseResult = JSON.parseObject(responseResult, WeixinResponseResult.class);
        if (weixinResponseResult.getErrcode() != 0) {
            log.error("授权凭证无效：" + responseResult);
            throw new MyException(weixinResponseResult.getErrcode(), weixinResponseResult.getErrmsg());
        }
        return weixinResponseResult;
    }

    @Override
    public String refreshAccessToken(String appId, String refreshToken) {
        String responseResult = restTemplate.getForObject(Constants.AUTH_REFRESH_ACCESS_TOKEN
                .replace("APPID", appId)
                .replace("REFRESH_TOKEN", refreshToken), String.class);
        JSONObject jsonObject = JSON.parseObject(responseResult);
        return jsonObject.getString("access_token");
    }

}
