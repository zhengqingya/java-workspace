package com.zhengqing.mybatis.executor.resultset;

import com.zhengqing.mybatis.mapping.MappedStatement;

import java.sql.PreparedStatement;
import java.util.List;

/**
 * <p> 结果处理器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/26 20:37
 */
public interface ResultSetHandler {
    
    <T> List<T> handleResultSets(MappedStatement ms, PreparedStatement ps);

}
