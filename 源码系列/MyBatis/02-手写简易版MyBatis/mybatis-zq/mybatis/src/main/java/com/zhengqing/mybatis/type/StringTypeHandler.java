package com.zhengqing.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p> String字段类型处理器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/22 03:00
 */
public class StringTypeHandler implements TypeHandler<String> {
    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter) throws SQLException {
        ps.setString(i, parameter);
    }

    @Override
    public String getResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }
}
