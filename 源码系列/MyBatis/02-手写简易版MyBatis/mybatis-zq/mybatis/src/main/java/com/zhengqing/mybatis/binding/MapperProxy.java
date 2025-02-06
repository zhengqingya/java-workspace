package com.zhengqing.mybatis.binding;

import com.google.common.collect.Maps;
import com.zhengqing.mybatis.annotations.Param;
import com.zhengqing.mybatis.mapping.MappedStatement;
import com.zhengqing.mybatis.mapping.SqlCommandType;
import com.zhengqing.mybatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * <p> mapper代理 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/21 19:57
 */
public class MapperProxy implements InvocationHandler {


    private SqlSession sqlSession;
    private Class mapperClass;

    public MapperProxy(SqlSession sqlSession, Class mapperClass) {
        this.sqlSession = sqlSession;
        this.mapperClass = mapperClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 获取mapper调用方法的参数名 -> 参数值
        Map<String, Object> paramValueMap = Maps.newHashMap();
        Parameter[] parameterList = method.getParameters();
        for (int i = 0; i < parameterList.length; i++) {
            Parameter parameter = parameterList[i];
            Param param = parameter.getAnnotation(Param.class);
            String paramName = param.value();
            paramValueMap.put(paramName, args[i]);
        }

        String statementId = this.mapperClass.getName() + "." + method.getName();
        MappedStatement ms = this.sqlSession.getConfiguration().getMappedStatement(statementId);
        SqlCommandType sqlCommandType = ms.getSqlCommandType();
        switch (sqlCommandType) {
            case INSERT:
                return this.convertResult(ms, this.sqlSession.insert(statementId, paramValueMap));
            case DELETE:
                return this.convertResult(ms, this.sqlSession.delete(statementId, paramValueMap));
            case UPDATE:
                return this.convertResult(ms, this.sqlSession.update(statementId, paramValueMap));
            case SELECT:
                if (ms.getIsSelectMany()) {
                    return this.sqlSession.selectList(statementId, paramValueMap);
                } else {
                    return this.sqlSession.selectOne(statementId, paramValueMap);
                }
            default:
                break;
        }
        return null;
    }

    private Object convertResult(MappedStatement ms, int updateCount) {
        Class returnType = ms.getReturnType();
        if (returnType == int.class || returnType == Integer.class) {
            return updateCount;
        } else if (returnType == Long.class) {
            return Long.valueOf(updateCount);
        } else if (returnType == void.class) {
            return null;
        }
        return null;
    }


}
