package com.zhengqing.demo;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.zhengqing.demo.config.AlipayConfigProperty;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>
 * 支付宝-普通调用：
 * 1. 设置参数（全局只需设置一次）。
 * 2. 发起API调用。
 * 3. 处理响应或异常。
 * </p>
 *
 * @author zhengqingya
 * @description 參考：https://github.com/alipay/alipay-easysdk/tree/master/java
 * @date 2021/7/12 10:39
 */
@Slf4j
@SpringBootTest
public class AlipaySimpleTest {

    @Autowired
    private AlipayConfigProperty alipayConfigProperty;

    @Test
    public void testSimple() throws Exception {
        // 1. 设置参数（全局只需设置一次）
        Factory.setOptions(this.getOptions());
        try {
            // 2. 发起API调用（以电脑网站支付为例）
            // 订单标题
            String subject = "Apple iPhone11 128G";
            // 交易创建时传入的商户订单号
            String outTradeNo = "009";
            // 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
            String totalAmount = "1.00";
            // 支付成功后同步跳转的页面，是一个http/https开头的字符串
            String returnUrl = alipayConfigProperty.getReturn_url();
            AlipayTradePagePayResponse alipayTradePagePayResponse = Factory.Payment.Page().pay(subject, outTradeNo, totalAmount, returnUrl);
            // 3. 处理响应或异常
            log.info("响应参数: {}", alipayTradePagePayResponse);
        } catch (Exception e) {
            log.error("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 设置支付宝配置参数
     *
     * @return 支付配置参数
     * @author zhengqingya
     * @date 2021/7/12 10:45
     */
    private Config getOptions() {
        Config config = new Config();
        config.protocol = "https";
        // TODO 网关域名 线上为：openapi.alipay.com 沙箱为：openapi.alipaydev.com
        config.gatewayHost = "openapi.alipaydev.com";
        config.signType = "RSA2";

        config.appId = alipayConfigProperty.getApp_id();

        // 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
        config.merchantPrivateKey = alipayConfigProperty.getMerchant_private_key();

        //注：证书文件路径支持设置为文件系统中的路径或CLASS_PATH中的路径，优先从文件系统中加载，加载失败后会继续尝试从CLASS_PATH中加载
//        config.merchantCertPath = "<-- 请填写您的应用公钥证书文件路径，例如：/foo/appCertPublicKey_2019051064521003.crt -->";
//        config.alipayCertPath = "<-- 请填写您的支付宝公钥证书文件路径，例如：/foo/alipayCertPublicKey_RSA2.crt -->";
//        config.alipayRootCertPath = "<-- 请填写您的支付宝根证书文件路径，例如：/foo/alipayRootCert.crt -->";

        //注：如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
        config.alipayPublicKey = alipayConfigProperty.getAlipay_public_key();

        //可设置异步通知接收服务地址（可选）
        config.notifyUrl = alipayConfigProperty.getNotify_url();

        //可设置AES密钥，调用AES加解密相关接口时需要（可选）
//        config.encryptKey = "<-- 请填写您的AES密钥，例如：aa4BtZ4tspm2wnXLb1ThQA== -->";
        return config;
    }

}
