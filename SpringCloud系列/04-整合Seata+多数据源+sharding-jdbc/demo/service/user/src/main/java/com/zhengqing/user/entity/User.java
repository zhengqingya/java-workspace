package com.zhengqing.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

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

    @ApiModelProperty(value = "用户名", example = "test")
    private String username;

    @ApiModelProperty(value = "密码", example = "123456")
    private String password;

    /**
     * sex值为空时，MP更新数据库时不忽略此字段值
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @ApiModelProperty(value = "性别", example = "1")
    private Byte sex;

    @ApiModelProperty(value = "备注", example = "this is the test data...")
    private String remark;

    @ApiModelProperty("创建时间")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date createTime;

}
