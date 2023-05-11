package com.zhengqing.common.base.model.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * <p> 基类参数 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/8/17 15:38
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("基类参数")
public class BaseBO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore // jackson
//    @JSONField(serialize = false, deserialize = false) // fastjson
    @ApiModelProperty(value = "隐藏字段-解决子类lombok部分注解(ex:构造器@NoArgsConstructor、@AllArgsConstructor)无法使用问题", hidden = true)
    private String xxx;

}
