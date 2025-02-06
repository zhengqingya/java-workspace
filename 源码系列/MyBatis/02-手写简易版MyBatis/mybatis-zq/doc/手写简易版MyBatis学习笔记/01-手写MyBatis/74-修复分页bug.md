# 修复分页bug

```java
import com.zhengqing.mybatis.executor.Executor;
import com.zhengqing.mybatis.mapping.MappedStatement;

@Intercepts({
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class})
})
public class LimitInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) {
//        System.out.println("分页插件start");
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        if (!ms.getSql().contains("LIMIT")) {
            ms.setSql(ms.getSql() + " LIMIT 2");
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
```