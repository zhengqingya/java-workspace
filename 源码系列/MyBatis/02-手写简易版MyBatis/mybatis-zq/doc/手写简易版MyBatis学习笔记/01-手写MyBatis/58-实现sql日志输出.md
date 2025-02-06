# 实现sql日志输出

#### 1、核心配置中新增sql日志打印插件

```java
public class Configuration {
    private InterceptorChain interceptorChain = new InterceptorChain();

    public Configuration() {
        // ...
        // 添加sql日志打印插件
        this.interceptorChain.addInterceptor(new SqlInterceptor());
    }
}
```

#### 2、完善sql日志打印插件

```java
import java.sql.PreparedStatement;

@Intercepts({
        @Signature(
                type = ResultSetHandler.class,
                method = "handleResultSets",
                args = {MappedStatement.class, PreparedStatement.class})
})
public class SqlInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) {
        System.out.println("sql插件start");
        PreparedStatement ps = (PreparedStatement) invocation.getArgs()[1];
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