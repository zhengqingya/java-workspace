package com.zhengqing.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p> 字段类型处理器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/22 02:58
 */
public interface TypeHandler<T> {

    /**
     * 设置值
     *
     * @param ps        PreparedStatement
     * @param i         参数位置
     * @param parameter 参数值
     * @return void
     * @author zhengqingya
     * @date 2024/4/22 16:19
     */
    void setParameter(PreparedStatement ps, int i, T parameter) throws SQLException;

    /**
     * 获取值
     *
     * @param rs         ResultSet结果集
     * @param columnName 字段名称
     * @return 字段值
     * @author zhengqingya
     * @date 2024/4/22 16:19
     */
    T getResult(ResultSet rs, String columnName) throws SQLException;

}
