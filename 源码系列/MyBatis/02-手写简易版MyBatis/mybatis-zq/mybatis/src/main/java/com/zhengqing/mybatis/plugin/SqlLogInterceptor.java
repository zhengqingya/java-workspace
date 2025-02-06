package com.zhengqing.mybatis.plugin;

import com.zhengqing.mybatis.executor.statement.StatementHandler;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * <p> sql日志插件 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/26 01:26
 */
@Intercepts({
        @Signature(
                type = StatementHandler.class,
                method = "query",
                args = Statement.class),
        @Signature(
                type = StatementHandler.class,
                method = "update",
                args = Statement.class)
})
@Slf4j
public class SqlLogInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) {
//        System.out.println("sql插件start");
        PreparedStatement ps = (PreparedStatement) invocation.getArgs()[0];
        String sql = ps.toString().replace("com.mysql.cj.jdbc.ClientPreparedStatement: ", "");
        System.err.println(sql);
        Object result = invocation.proceed();
//        System.out.println("sql插件end");
        return result;
    }

    @Override
    public <T> T plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
