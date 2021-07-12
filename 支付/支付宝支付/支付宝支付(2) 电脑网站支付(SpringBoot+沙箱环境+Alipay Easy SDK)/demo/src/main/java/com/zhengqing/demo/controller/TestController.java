package com.zhengqing.demo.controller;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.google.common.collect.Maps;
import com.zhengqing.demo.config.AlipayConfigProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p> 测试支付接口 </p>
 *
 * @author zhengqingya
 * @description https://github.com/alipay/alipay-easysdk/blob/master/APIDoc.md
 * 统一收单下单并支付页面接口 https://opendocs.alipay.com/apis/api_1/alipay.trade.page.pay
 * @date 2021/7/10 6:48 下午
 */
@Slf4j
@Controller
@RequestMapping("")
@Api(tags = {"测试支付接口"})
public class TestController {

    @Autowired
    private AlipayConfigProperty alipayConfigProperty;

    @PostConstruct
    public void beforeInit() {
        // 设置参数（全局只需设置一次）
        Factory.setOptions(this.getOptions());
    }

    @GetMapping("/index")
    public String index() {
        return "index.html";
    }

    // http://127.0.0.1:8080/alipay
    @ApiOperation("支付")
    @GetMapping(value = "alipay")
    @ResponseBody
    @SneakyThrows(Exception.class)
    public String alipay() {
        try {
            // 1、发起API调用（以电脑网站支付为例）
            // 订单标题
            String subject = "Apple iPhone11 128G";
            // 交易创建时传入的商户订单号
            String outTradeNo = "012";
            // 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
            String totalAmount = "1.00";
            // 支付成功后同步跳转的页面，是一个http/https开头的字符串
            String returnUrl = alipayConfigProperty.getReturn_url();
            AlipayTradePagePayResponse alipayTradePagePayResponse = Factory.Payment.Page().pay(subject, outTradeNo, totalAmount, returnUrl);
            // 2、处理响应或异常
            log.info("[支付] 请求响应结果：\n {}", alipayTradePagePayResponse.getBody());
            return alipayTradePagePayResponse.getBody();
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


    @ApiOperation("支付成功回调")
    @GetMapping(value = "success")
    @SneakyThrows(Exception.class)
    public String alipaySuccess(HttpServletRequest request) {
        //获取支付宝GET过来反馈信息
        Map<String, String> params = Maps.newHashMap();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        // 验签
        Boolean signVerified = Factory.Payment.Common().verifyNotify(params);
        // 请在这里编写您的程序（以下代码仅作参考）——
        if (signVerified) {
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
            //付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");
            log.info("[支付成功回调] 订单信息：trade_no:{} out_trade_no:{} total_amount:{}", trade_no, out_trade_no, total_amount);
            return "success.html";
        } else {
            log.error("验签失败");
            return "error.html";
        }
    }

}
