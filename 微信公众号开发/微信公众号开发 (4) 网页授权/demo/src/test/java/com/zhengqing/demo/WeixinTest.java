package com.zhengqing.demo;

import com.zhengqing.demo.config.Constants;
import com.zhengqing.demo.modules.weixin.model.AccessTokenVO;
import com.zhengqing.demo.modules.weixin.service.IWeixinService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * <p> 微信接口测试$ </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/16$ 16:40$
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class WeixinTest {

    @Autowired
    private IWeixinService weixinService;

    @Test // 获取 `access_token`
    public void getAccessToken() throws Exception {
        AccessTokenVO accessTokenVO = weixinService.getAccessToken(Constants.APP_ID, Constants.APP_SECRET);
        log.info("======================================== \n" + accessTokenVO.getAccess_token());
    }

    @Test // 回调地址加密
    public void testUrlEncode() throws Exception {
        try {
            String encode = URLEncoder.encode(Constants.AUTH_REDIRECT_URI, "UTF-8");
            System.out.println(encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test // 刷新access_token
    public void testRefreshAccessToken() throws Exception {
        String refreshToken = "xxx";
        String accessToken = weixinService.refreshAccessToken(Constants.APP_ID, refreshToken);
        log.debug(accessToken);
    }

}
