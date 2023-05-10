package com.zhengqing.common.base.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * <p> 基类响应参数 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/8/18 16:14
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("基类响应参数")
public class BaseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @ApiModelProperty(value = "隐藏字段-解决子类lombok部分注解(ex:构造器@NoArgsConstructor、@AllArgsConstructor)无法使用问题", hidden = true)
    private String xxx;

}
