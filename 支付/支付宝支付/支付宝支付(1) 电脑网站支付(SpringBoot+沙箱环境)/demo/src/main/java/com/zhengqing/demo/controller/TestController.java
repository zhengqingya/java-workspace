package com.zhengqing.demo.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.zhengqing.demo.alipay.AlipaySuccessDTO;
import com.zhengqing.demo.config.AlipayConfigProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p> 测试支付接口 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/7/10 6:48 下午
 */
@Slf4j
@Controller
@RequestMapping("")
@Api(tags = {"测试支付接口"})
public class TestController {

    @Autowired
    private AlipayConfigProperty alipayConfigProperty;

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
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfigProperty.gatewayUrl, alipayConfigProperty.app_id, alipayConfigProperty.merchant_private_key, "json", alipayConfigProperty.charset, alipayConfigProperty.alipay_public_key, alipayConfigProperty.sign_type);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(alipayConfigProperty.return_url);
        alipayRequest.setNotifyUrl(alipayConfigProperty.notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = "008";
        //付款金额，必填
        String total_amount = "1";
        //订单名称，必填
        String subject = "测试-商品名称";
        //商品描述，可空
        String body = "测试-商品描述";

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
        //		+ "\"total_amount\":\""+ total_amount +"\","
        //		+ "\"subject\":\""+ subject +"\","
        //		+ "\"body\":\""+ body +"\","
        //		+ "\"timeout_express\":\"10m\","
        //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        log.info("[支付] 请求响应结果：{}", result);
        return result;
    }


    @ApiOperation("支付成功回调")
    @GetMapping(value = "success")
    @SneakyThrows(Exception.class)
    public String alipaySuccess(HttpServletRequest request, @ModelAttribute AlipaySuccessDTO dto) {
        //获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
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
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayConfigProperty.alipay_public_key, alipayConfigProperty.charset, alipayConfigProperty.sign_type); //调用SDK验证签名

        // 请在这里编写您的程序（以下代码仅作参考）——
        if (signVerified) {
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

            log.info("trade_no:" + trade_no + "<br/>out_trade_no:" + out_trade_no + "<br/>total_amount:" + total_amount);
            log.info("订单信息：{}", dto);
            return "success.html";
        } else {
            log.error("验签失败");
            return "error.html";
        }
    }

}
