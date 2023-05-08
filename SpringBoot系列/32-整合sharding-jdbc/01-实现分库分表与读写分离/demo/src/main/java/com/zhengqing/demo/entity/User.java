package com.zhengqing.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
@ApiModel("用户")
@EqualsAndHashCode(callSuper = true)
public class User extends Model<User> {

    @ApiModelProperty("主键ID")
    @TableId(type = IdType.AUTO)
    private Long userId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    /**
     * sex值为空时，MP更新数据库时不忽略此字段值
     */
    @TableField(value = "sex", updateStrategy = FieldStrategy.IGNORED)
    @ApiModelProperty("性别")
    private Byte sex;

    @ApiModelProperty("备注")
    private String remark;

    @TableField(exist = false)
    @ApiModelProperty(value = "当前页数",  example = "1")
    private Integer page;

    @TableField(exist = false)
    @ApiModelProperty(value = "分页大小",  example = "10")
    private Integer pageSize;

}
