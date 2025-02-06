# 完善sql日志插件（支持增删改查所有sql日志）

```java
import com.zhengqing.mybatis.executor.statement.StatementHandler;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.Statement;

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
        System.out.println("sql插件start");
        PreparedStatement ps = (PreparedStatement) invocation.getArgs()[0];
        String sql = ps.toString().replace("com.mysql.cj.jdbc.ClientPreparedStatement: ", "");
        System.err.println(sql);
        Object result = invocation.proceed();
        System.out.println("sql插件end");
        return result;
    }

    @Override
    public <T> T plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
```