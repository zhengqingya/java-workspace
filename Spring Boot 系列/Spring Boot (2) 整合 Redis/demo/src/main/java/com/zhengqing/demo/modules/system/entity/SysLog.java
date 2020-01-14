package com.zhengqing.demo.modules.system.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.zhengqing.demo.modules.common.entity.BaseEntity;
import com.zhengqing.demo.modules.common.validator.TextFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>  系统管理 - 日志表 </p>
 *
 * @author: zhengqing
 * @date: 2019-09-18 10:51:57
 */
@Data
@ApiModel(description = "系统管理 - 日志表")
@TableName("t_sys_log")
public class SysLog extends BaseEntity<SysLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 接口名称
     */
    @NotNull(message = "不能为空！")
    @TextFormat(notChinese = true, notContains = {"Z"}, startWith = "start", endsWith = "end", message = "name字段参数不合法")
    @ApiModelProperty(value = "接口名称")
    private String name;
    /**
     * 接口地址
     */
    @ApiModelProperty(value = "接口地址")
    @TableField("url")
    private String url;
    /**
     * 接口执行时间
     */
    @ApiModelProperty(value = "接口执行时间")
    @TableField("execute_time")
    private String executeTime;
    /**
     * 访问人IP
     */
    @ApiModelProperty(value = "访问人IP")
    @TableField("ip")
    private String ip;
    /**
     * 访问人ID
     */
    @ApiModelProperty(value = "访问人ID")
    @TableField("user_id")
    private Integer userId;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    @TableField("status")
    private Integer status;
    /**
     * 创建日期 - 现在时表示主动创建
     */
    @ApiModelProperty(value = "创建日期")
    @TableField(value = "gmt_create")
    private Date gmtCreate;
    /**
     * 修改时间 - 过去分词表示被动更新
     */
    @ApiModelProperty(value = "修改时间")
    @TableField(value = "gmt_modified")
    private Date gmtModified;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
