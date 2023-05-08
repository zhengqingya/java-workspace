package com.zhengqing.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p> 支付宝支付-基础配置类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/7/10 6:38 下午
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "alipay", ignoreUnknownFields = false)
public class AlipayConfigProperty {

    /**
     * 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
     */
    public String app_id;

    /**
     * 商户私钥，您的PKCS8格式RSA2私钥
     */
    public String merchant_private_key;

    /**
     * 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
     */
    public String alipay_public_key;

    /**
     * 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     */
    public String notify_url;

    /**
     * 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     */
    public String return_url;

    /**
     * 签名方式
     */
    public String sign_type;

    /**
     * 字符编码格式
     */
    public String charset;

    /**
     * 支付宝网关
     */
    public String gatewayUrl;

    /**
     * 日志路径
     */
    public String log_path;

}
