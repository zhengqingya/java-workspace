package com.zhengqing.demo.modules.weixin.service.impl;

import com.zhengqing.demo.config.Constants;
import com.zhengqing.demo.modules.weixin.model.AccessTokenVO;
import com.zhengqing.demo.modules.weixin.service.IWeixinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

}
