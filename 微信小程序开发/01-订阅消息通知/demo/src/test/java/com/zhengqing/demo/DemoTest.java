package com.zhengqing.demo;

import com.zhengqing.demo.model.bo.WxMaTemplateMsgBO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * <p> 小测试 </p>
 *
 * @author zhengqingya
 * @description 可参考：https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/subscribe-message.html
 * @date 2021/7/6 9:38
 */
@Slf4j
public class DemoTest {

    @Test
    public void testPushWxMaMsg() {
        // TODO 1、填写自己的数据
        final String APPID = "xxx";
        final String APPSECRET = "xxx";
        final String touser = "xxx";
        final String template_id = "xxx";
        final String url = "pages/index/index";
        final HashMap<String, Object> templateDataMap = new HashMap<String, Object>() {
            {
                put("thing1", new HashMap<String, String>() {{
                    put("value", "内容简述-test");
                }});
                put("thing2", new HashMap<String, String>() {{
                    put("value", "学习内容-test");
                }});
            }
        };

        // 2、发送订阅消息
        Map<String, String> resultMap = this.sendWxMaMsg(WxMaTemplateMsgBO.builder()
                .access_token(this.getAccessToken(APPID, APPSECRET))
                .touser(touser)
                .template_id(template_id)
                .page(url)
                .miniprogram_state("developer")
                .data(templateDataMap)
                .lang("zh_CN")
                .build());
        log.info("小程序-发送订阅消息结果：{}", resultMap);
    }

    /**
     * 小程序-发送订阅消息
     *
     * @param wxMaTemplateMsgBO: 请求参数
     * @return 通知结果
     * @author zhengqingya
     * @date 2021/7/6 10:13
     */
    public Map<String, String> sendWxMaMsg(WxMaTemplateMsgBO wxMaTemplateMsgBO) {
        String sendWxMaMsgRequestUrl = String.format("https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=%s", wxMaTemplateMsgBO.getAccess_token());
        RestTemplate restTemplate = new RestTemplate();
        // 解决post请求中文乱码问题
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        // 注：如果报`{"errcode":47001,"errmsg":"data format error rid: 61334bd2-26ac356f-0eb9e0f6"}`  使用：wxMaTemplateMsgBO => JSON.toJSONString(wxMaTemplateMsgBO)
//        return restTemplate.postForEntity(sendWxMaMsgRequestUrl, JSON.toJSONString(wxMaTemplateMsgBO), Map.class).getBody();
        return restTemplate.postForEntity(sendWxMaMsgRequestUrl, wxMaTemplateMsgBO, Map.class).getBody();
    }


    /**
     * 获取小程序全局唯一后台接口调用凭据（access_token）
     *
     * @param appid:  小程序唯一凭证，即 AppID，可在「微信公众平台 - 设置 - 开发设置」页中获得。（需要已经成为开发者，且帐号没有异常状态）
     * @param secret: 小程序唯一凭证密钥，即 AppSecret，获取方式同 appid
     * @return 调用凭据
     * @author zhengqingya
     * @date 2021/7/6 10:04
     */
    private String getAccessToken(String appid, String secret) {
        String accessTokenRequestUrl = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", appid, secret);
        Map<String, String> tokenMap = new RestTemplate().getForObject(accessTokenRequestUrl, Map.class);
        return CollectionUtils.isEmpty(tokenMap) ? "" : tokenMap.get("access_token");
    }

}

