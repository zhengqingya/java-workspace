package com.zhengqing.demo.kdniao.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 快递鸟-物流-查询base参数
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
@ApiModel("快递鸟-物流-查询base参数")
public class KdniaoApiBaseDTO {

    @ApiModelProperty(value = "用户ID", required = true, example = "xx")
    private String eBusinessID;

    @ApiModelProperty(value = "API key", required = true, example = "xx")
    private String apiKey;

    @ApiModelProperty(value = "请求url", required = true, example = "https://api.kdniao.com/Ebusiness/EbusinessOrderHandle.aspx")
    private String reqURL;

}
