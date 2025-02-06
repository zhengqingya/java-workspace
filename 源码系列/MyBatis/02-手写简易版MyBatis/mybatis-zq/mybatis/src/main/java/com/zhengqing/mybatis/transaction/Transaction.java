package com.zhengqing.mybatis.transaction;

import java.sql.Connection;

/**
 * <p> 事务管理 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/27 21:59
 */
public interface Transaction {

    Connection getConnection();

    void commit();

    void rollback();

    void close();

}
