package com.zhengqing.common.base.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * <p>
 * 基类查询参数
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
@ApiModel("基类查询参数")
public class BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore // 字段忽略
//    @JsonProperty("userId") // 字段别名
    @ApiModelProperty(value = "当前用户ID", hidden = true)
    private Integer currentUserId;

    @JsonIgnore
    @ApiModelProperty(value = "当前用户名称", hidden = true)
    private String currentUsername;

//    @JsonIgnore
//    @ApiModelProperty(value = "令牌", hidden = true)
//    private String token;

}
