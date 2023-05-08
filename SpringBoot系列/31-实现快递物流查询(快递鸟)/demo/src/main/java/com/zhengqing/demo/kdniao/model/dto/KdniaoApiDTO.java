package com.zhengqing.demo.kdniao.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 快递鸟-物流-查询参数
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
@EqualsAndHashCode(callSuper = true)
@ApiModel("快递鸟-物流-查询参数")
public class KdniaoApiDTO extends KdniaoApiBaseDTO {

    @ApiModelProperty(value = "快递公司编码", required = true, example = "ZTO")
    private String shipperCode;

    @ApiModelProperty(value = "快递单号", required = true, example = "xxx")
    private String logisticCode;

}
