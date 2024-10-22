package com.zhengqing.spring.jdbc;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementCallback<T> {

    @Nullable
    T doInPreparedStatement(PreparedStatement ps) throws SQLException;

}
