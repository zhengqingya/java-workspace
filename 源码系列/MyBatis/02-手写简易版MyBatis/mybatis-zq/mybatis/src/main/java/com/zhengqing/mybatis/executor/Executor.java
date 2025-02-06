package com.zhengqing.mybatis.executor;

import com.zhengqing.mybatis.mapping.MappedStatement;

import java.util.List;

/**
 * <p> SQL执行器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/22 23:07
 */
public interface Executor {

    <T> List<T> query(MappedStatement ms, Object parameter);

    int update(MappedStatement ms, Object parameter);

    void commit();

    void rollback();

    void close();

}
