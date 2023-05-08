package com.zhengqing.demo.canal.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> canal消息体 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/1/20 17:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("canal消息体")
public class CanalMsg {


    @ApiModelProperty(value = "id")
    private Integer id;

}
