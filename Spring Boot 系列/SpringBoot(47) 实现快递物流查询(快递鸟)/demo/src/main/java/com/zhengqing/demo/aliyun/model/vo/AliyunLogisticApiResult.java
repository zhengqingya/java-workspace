package com.zhengqing.demo.aliyun.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> 物流-api响应结果 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/10/23 9:19 下午
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("物流-api响应结果")
public class AliyunLogisticApiResult {

    @ApiModelProperty("状态码")
    private Integer status;

    @ApiModelProperty("提示信息")
    private String msg;

    @ApiModelProperty("结果集")
    private AliyunLogisticVO result;

}
