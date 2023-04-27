package com.zhengqing.demo.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * <p> 支付-订单创建-响应参数 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/08/20 17:38
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PayOrderCreateVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("时间戳从1970年1月1日至今的秒数，即当前的时间。")
    private String timeStamp;

    @ApiModelProperty("随机字符串，长度为32个字符以下。")
    private String nonceStr;

    @ApiModelProperty("统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=xx。")
    private String wxPackage;

    @ApiModelProperty("签名算法，应与后台下单时的值一致")
    private String signType;

    @ApiModelProperty("签名")
    private String paySign;

    // --------------------------------------------

    /**
     * trade_type为NATIVE时有返回，用于生成二维码，展示给用户进行扫码支付
     */
    @ApiModelProperty("生成二维码-扫码支付")
    private String codeUrl;

}
