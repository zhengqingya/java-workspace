package com.zhengqing.demo.modules.system.entity;

import lombok.Data;

/**
 * <p> 数据库表信息 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2019/11/8 16:28
 */
@Data
public class Tables {

    /**
     * 表名
     */
    private String name;
    /**
     * 表注释
     */
    private String comment;

}
