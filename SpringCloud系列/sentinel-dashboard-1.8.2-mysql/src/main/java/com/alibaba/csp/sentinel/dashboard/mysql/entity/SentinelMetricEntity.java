package com.alibaba.csp.sentinel.dashboard.mysql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.util.Date;

/**
 * <p> sentinel </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/10/3 7:31 下午
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_sentinel_metric")
public class SentinelMetricEntity extends Model<SentinelMetricEntity> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Date gmtCreate;
    private Date gmtModified;
    private String app;
    private Date timestamp;
    private String resource;
    private Long passQps;
    private Long successQps;
    private Long blockQps;
    private Long exceptionQps;
    private double rt;
    private int count;
    private int resourceCode;

}
