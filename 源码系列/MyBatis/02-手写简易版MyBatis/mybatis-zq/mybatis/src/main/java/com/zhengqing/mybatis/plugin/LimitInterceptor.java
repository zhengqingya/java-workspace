package com.zhengqing.mybatis.plugin;

import com.zhengqing.mybatis.executor.statement.PreparedStatementHandler;
import com.zhengqing.mybatis.executor.statement.StatementHandler;
import com.zhengqing.mybatis.mapping.BoundSql;

import java.sql.Connection;

/**
 * <p> 分页插件 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/26 01:26
 */
@Intercepts({
        @Signature(
                type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class})
})
public class LimitInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) {
//        System.out.println("分页插件start");
        PreparedStatementHandler psh = (PreparedStatementHandler) invocation.getTarget();
        BoundSql boundSql = psh.getBoundSql();
        String sql = boundSql.getSql();
        if (sql.contains("select") && !sql.contains("LIMIT")) {
            boundSql.setSql(sql + " LIMIT 2");
        }
        Object result = invocation.proceed();
//        System.out.println("分页插件end");
        return result;
    }

    @Override
    public <T> T plugin(Object target) {
        return Plugin.wrap(target, this);
    }

}
