package com.zhengqing.mybatis.executor.resultset;

import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Lists;
import com.zhengqing.mybatis.mapping.MappedStatement;
import com.zhengqing.mybatis.session.Configuration;
import com.zhengqing.mybatis.type.TypeHandler;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Map;

/**
 * <p> 默认结果处理器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/26 20:38
 */
public class DefaultResultSetHandler implements ResultSetHandler {

    private Configuration configuration;

    public DefaultResultSetHandler(Configuration configuration) {
        this.configuration = configuration;
    }

    @SneakyThrows
    @Override
    public <T> List<T> handleResultSets(MappedStatement ms, PreparedStatement ps) {
        // 拿到mapper的返回类型
        Class returnType = ms.getReturnType();

        // 拿到结果集
        ResultSet rs = ps.getResultSet();

        // 拿到sql返回字段名称
        List<String> columnList = Lists.newArrayList();
        ResultSetMetaData metaData = rs.getMetaData();
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            columnList.add(metaData.getColumnName(i + 1));
        }

        Map<Class, TypeHandler> typeHandlerMap = this.configuration.getTypeHandlerMap();
        List list = Lists.newArrayList();
        while (rs.next()) {
            // 结果映射
            Object instance = returnType.newInstance();
            for (String columnName : columnList) {
                Field field = ReflectUtil.getField(returnType, columnName);
                Object val = typeHandlerMap.get(field.getType()).getResult(rs, columnName);
                ReflectUtil.setFieldValue(instance, columnName, val);
            }
            list.add(instance);
        }
        rs.close();
        ps.close();
        return list;
    }

}
