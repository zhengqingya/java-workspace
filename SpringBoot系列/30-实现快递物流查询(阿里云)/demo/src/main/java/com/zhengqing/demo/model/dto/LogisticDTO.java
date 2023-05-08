package com.zhengqing.demo.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 物流-查询参数
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/10/23 9:19 下午
 */
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
