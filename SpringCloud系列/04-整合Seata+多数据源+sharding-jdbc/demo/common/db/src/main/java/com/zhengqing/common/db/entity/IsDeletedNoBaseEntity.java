package com.zhengqing.common.db.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * BaseEntity
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2019/8/18 1:30
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class IsDeletedNoBaseEntity<T extends Model<T>> extends BaseEntity<T> {

    //    @TableLogic
    @ApiModelProperty(value = "是否删除：true->删除，false->未删除")
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    private Boolean isDeleted;

}
