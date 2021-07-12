### 一、前言

1. [支付宝支付(1) 电脑网站支付(SpringBoot+沙箱环境)](https://zhengqing.blog.csdn.net/article/details/118575809)
2. [Alipay Easy SDK 文档](https://opendocs.alipay.com/open/54/00y8k9)
3. Alipay Easy -> [https://github.com/alipay/alipay-easysdk](https://github.com/alipay/alipay-easysdk)

> 注：本文只是简单的介绍Alipay Easy SDK的简单使用 `^_^`

### 二、获取所需配置参数信息

> 注：这个所需参数信息，参考下之前的文章即可，这里不多说

1. APPID
2. 支付宝网关
3. 支付宝公钥
4. RSA2私钥

### 三、电脑网站支付-SpringBoot版Demo

> 可参考：[https://github.com/alipay/alipay-easysdk/tree/master/java](https://github.com/alipay/alipay-easysdk/tree/master/java)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210712164821551.png)


#### 1、`pom.xml`中新增依赖

```xml
<!-- alipay-easysdk -->
<!-- https://mvnrepository.com/artifact/com.alipay.sdk/alipay-easysdk -->
<dependency>
    <groupId>com.alipay.sdk</groupId>
    <artifactId>alipay-easysdk</artifactId>
    <version>2.2.0</version>
</dependency>
```

#### 2、`application.yml`中添加配置

> 根据自己的配置修改即可

```yml
alipay:
  # 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
  app_id: xxx
  # 商户私钥，您的PKCS8格式RSA2私钥
  merchant_private_key: xxx
  # 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
  alipay_public_key: xxx
  # 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数 [需外网能访问]
  notify_url: http://mfn68i.natappfree.cc/error.html
  # 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数 [需外网能访问]
  return_url: http://mfn68i.natappfree.cc/success
  # 签名方式
  sign_type: RSA2
  # 字符编码格式
  charset: utf-8
  # 支付宝网关
  gatewayUrl: https://openapi.alipaydev.com/gateway.do
  # 日志路径
  log_path: "log/"
```

#### 3、配置类

```java
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
```

#### 4、测试支付接口

```java
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
```

#### 5、回调成功/失败 html页面

success.html

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title></title>
</head>
<body>
<h1>SUCCESS</h1>
</body>
</html>
```

error.html

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title></title>
</head>
<body>
<h1>ERROR</h1>
</body>
</html>
```


#### 6、运行

调用支付接口 [http://127.0.0.1:8080/alipay](http://127.0.0.1:8080/alipay)

> 温馨小提示：如果出现提示`支付存在钓鱼风险！` ，解决：换个浏览器清除所有缓存再次尝试即可！
> ![在这里插入图片描述](https://img-blog.csdnimg.cn/20210710154005866.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210712164148567.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)

支付成功后，会回调我们配置的`return_url`地址，然后处理自己的业务即可...

### 四、本文案例demo源码

[https://gitee.com/zhengqingya/java-workspace](https://gitee.com/zhengqingya/java-workspace)

---

> 今日分享语句：
> 世界上一切的成功，都将通过勤奋来实现。
> 勤奋，代表着人们博弈人生的动力；
> 成功，却是这种动力下产出的结果。
> 没有勤奋，就没有成功，即便你是一个天资聪颖、淋漓尽致的人。
