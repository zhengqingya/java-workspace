@[TOC](文章目录)

### 一、前言

本文将基于springboot2.4.0实现快递物流查询，物流信息的获取通过阿里云第三方实现

> 可参考: [https://market.aliyun.com/products/57124001/cmapi022273.html?spm=5176.730005.productlist.d_cmapi022273.e8357d36FVX3Eu&innerSource=search#sku=yuncode1627300000](https://market.aliyun.com/products/57124001/cmapi022273.html?spm=5176.730005.productlist.d_cmapi022273.e8357d36FVX3Eu&innerSource=search#sku=yuncode1627300000)
![在这里插入图片描述](https://img-blog.csdnimg.cn/0e5b938b237c478dbdb54d2c1bbb22e4.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA6YOR5riF,size_20,color_FFFFFF,t_70,g_se,x_16)

快递查询API，快递识别单号，快递接口可查询上百家快递公司及物流快递信息包括：顺丰、申通、圆通、韵达、中通、汇通、EMS、天天、国通、德邦、宅急送等几百家快递物流公司单号查询接口。与官网实时同步更新，包含快递送达时间。

### 二、快递物流查询

> 注：需要购买快递物流查询接口服务获取`AppCode`
> ![在这里插入图片描述](https://img-blog.csdnimg.cn/726cac88299d41a6bebfd8ad40ed9958.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA6YOR5riF,size_20,color_FFFFFF,t_70,g_se,x_16)


工具类

> 其中http请求工具类自行查看demo源码

```java

@Slf4j
public class LogisticUtil {

    /**
     * 查询物流信息
     *
     * @param params 提交参数
     * @return 物流信息
     * @author zhengqingya
     * @date 2021/10/23 10:48 下午
     */
    public static LogisticVO getLogisticInfo(LogisticDTO params) {
        String no = params.getNo();
        String type = params.getType();
        String appCode = params.getAppCode();

        // 请求地址
        String requestUrl = String.format("https://kdwlcxf.market.alicloudapi.com/kdwlcx?no=%s&type=%s",
                no, StringUtils.isBlank(type) ? "" : type);
        // 发起请求
        Map<String, String> headerMap = Maps.newHashMap();
        headerMap.put("Authorization", String.format("APPCODE %s", appCode));
        String resultJson = HttpUtil.getUrl(requestUrl, headerMap);
        LogisticApiResult logisticApiResult = JSON.parseObject(resultJson, LogisticApiResult.class);
        Assert.notNull(logisticApiResult, "参数异常");
        Assert.isTrue(logisticApiResult.getStatus() == 0, logisticApiResult.getMsg());
        return logisticApiResult.getResult();
    }

}
```

请求实体类

```java

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("物流-查询参数")
public class LogisticDTO {

    @ApiModelProperty(value = "快递单号 【顺丰请输入运单号 : 收件人或寄件人手机号后四位。例如：123456789:1234】", required = true, example = "780098068058")
    private String no;

    @ApiModelProperty(value = "快递公司代码: 可不填自动识别，填了查询更快【代码见附表】", required = true, example = "zto")
    private String type;

    @ApiModelProperty(value = "appCode", required = true, example = "xxx")
    private String appCode;

}
```

响应实体类

```java

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("物流-api响应结果")
public class LogisticApiResult {

    @ApiModelProperty("状态码")
    private Integer status;

    @ApiModelProperty("提示信息")
    private String msg;

    @ApiModelProperty("结果集")
    private LogisticVO result;

}
```

```java

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("物流-响应参数")
public class LogisticVO {

    @ApiModelProperty("运单编号")
    private String number;

    @ApiModelProperty("快递公司编码[见附表]")
    private String type;

    @ApiModelProperty("投递状态 0快递收件(揽件)1在途中 2正在派件 3已签收 4派送失败 5.疑难件 6.退件签收")
    private String deliverystatus;

    @ApiModelProperty("是否本人签收")
    private String issign;

    @ApiModelProperty("快递公司名字")
    private String expName;

    @ApiModelProperty("快递公司官网")
    private String expSite;

    @ApiModelProperty("快递公司电话")
    private String expPhone;

    @ApiModelProperty("快递员")
    private String courier;

    @ApiModelProperty("快递员电话")
    private String courierPhone;

    @ApiModelProperty("最新轨迹的时间")
    private String updateTime;

    @ApiModelProperty("发货到收货耗时(截止最新轨迹)")
    private String takeTime;

    @ApiModelProperty("快递公司logo")
    private String logo;

    @ApiModelProperty("事件轨迹集")
    private List<LogisticItem> list;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel("事件轨迹集")
    public static class LogisticItem {
        @ApiModelProperty("时间点")
        private String time;

        @ApiModelProperty("事件详情")
        private String status;
    }

}
```

### 三、测试api

```java

@Slf4j
@RestController
@RequestMapping("/test")
@Api(tags = "测试api")
public class TestController {

    @ApiOperation("查询物流信息")
    @GetMapping("getLogistic")
    public LogisticVO getLogistic(@ModelAttribute LogisticDTO params) {
        return LogisticUtil.getLogisticInfo(params);
    }

}
```

接口文档 [http://127.0.0.1/doc.html](http://127.0.0.1/doc.html)
![在这里插入图片描述](https://img-blog.csdnimg.cn/65b0902c0f3d408d98fe4e1e3f0430b1.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA6YOR5riF,size_20,color_FFFFFF,t_70,g_se,x_16)

### 本文demo源码

[https://gitee.com/zhengqingya/java-workspace](https://gitee.com/zhengqingya/java-workspace)


---

> 今日分享语句：
> 相信梦想是价值的源泉，
> 相信眼光决定未来的一切，
> 相信成功的信念比成功本身更重要，
> 相信人生有挫折没有失败，
> 相信生命的质量来自决不妥协的信念。
