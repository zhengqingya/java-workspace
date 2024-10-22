package com.zhengqing.spring.jdbc;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapper<T> {

    @Nullable
    T mapRow(ResultSet rs, int rowNum) throws SQLException;

}
