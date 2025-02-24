package com.zhengqing.application.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 基础分页检索参数
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2019/9/13 0013 1:57
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("基础分页检索参数")
public class BasePageReq {

    @ApiModelProperty(value = "当前页", required = true, position = 0, example = "1")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页显示数量", required = true, position = 1, example = "10")
    private Integer pageSize = 10;

}
