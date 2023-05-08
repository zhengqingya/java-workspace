package com.zhengqing.demo.mysql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> MySQL变更数据 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/2/22 18:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MysqlDataBO {

    /**
     * 数据库
     */
    private String database;
    /**
     * 表
     */
    private String table;
    /**
     * 操作类型
     */
    private String operationType;
    /**
     * 变更前数据
     */
    private String before;
    /**
     * 变更后数据
     */
    private String after;

}
