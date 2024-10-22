package com.zhengqing.spring.jdbc;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface ConnectionCallback<T> {

    @Nullable
    T doInConnection(Connection con) throws SQLException;

}
