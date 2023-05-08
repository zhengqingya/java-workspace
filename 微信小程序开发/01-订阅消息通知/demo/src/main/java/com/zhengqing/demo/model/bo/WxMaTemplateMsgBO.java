package com.zhengqing.demo.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * <p> 微信小程序模板消息 </p>
 *
 * @author zhengqingya
 * @description 可参考: https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/subscribe-message.html
 * @date 2021/7/6 9:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("微信小程序模板消息")
public class WxMaTemplateMsgBO {

    @ApiModelProperty(value = "接口调用凭证")
    private String access_token;

    @ApiModelProperty(value = "接收者（用户）的 openid")
    private String touser;

    @ApiModelProperty(value = "所需下发的订阅模板id")
    private String template_id;

    @ApiModelProperty(value = "点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。")
    private String page;

    @ApiModelProperty(value = "跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版")
    private String miniprogram_state;

    @ApiModelProperty(value = "模板内容，格式形如 { \"key1\": { \"value\": any }, \"key2\": { \"value\": any } }")
    private Map<String, Object> data;

    @ApiModelProperty(value = "进入小程序查看”的语言类型，支持zh_CN(简体中文)、en_US(英文)、zh_HK(繁体中文)、zh_TW(繁体中文)，默认为zh_CN")
    private String lang;

}
