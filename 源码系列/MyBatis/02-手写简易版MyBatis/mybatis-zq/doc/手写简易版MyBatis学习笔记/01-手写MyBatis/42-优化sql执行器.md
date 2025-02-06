# 优化sql执行器

执行sql & 参数映射

```java
public class BoundSql {
    private String sql;
    private List<String> parameterMappings;
}
```

mapper配置信息 新增获取执行sql方法

```java
public class MappedStatement {
    // ...
    public BoundSql getBoundSql() {
        // sql解析  #{}  --- ?
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        String sql = genericTokenParser.parse(this.sql);
        List<String> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        return BoundSql.builder().sql(sql).parameterMappings(parameterMappings).build();
    }
}
```

sql执行器

```java
import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Lists;
import com.zhengqing.mybatis.mapping.BoundSql;
import com.zhengqing.mybatis.mapping.MappedStatement;
import com.zhengqing.mybatis.session.Configuration;
import com.zhengqing.mybatis.type.TypeHandler;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class SimpleExecutor implements Executor {

    private Configuration configuration;

    public SimpleExecutor(Configuration configuration) {
        this.configuration = configuration;
    }

    @SneakyThrows
    @Override
    public <T> List<T> query(MappedStatement ms, Object parameter) {
        Connection connection = getConnection();
        PreparedStatement ps = this.execute(connection, ms, parameter);
        List<T> list = this.handleResultSets(ms, ps);
        connection.close();
        return list;
    }

    @SneakyThrows
    @Override
    public int update(MappedStatement ms, Object parameter) {
        Connection connection = getConnection();
        PreparedStatement ps = this.execute(connection, ms, parameter);
        int updateCount = ps.getUpdateCount();   // 拿到操作数
        ps.close();
        connection.close();
        return updateCount;
    }

    @SneakyThrows
    private PreparedStatement execute(Connection connection, MappedStatement ms, Object parameter) {
        // 构建sql & 执行
        BoundSql boundSql = ms.getBoundSql();
        PreparedStatement ps = connection.prepareStatement(boundSql.getSql());
        this.setParam(ps, parameter, boundSql.getParameterMappings());

        ps.execute();
        return ps;
    }

    @SneakyThrows
    private static Connection getConnection() {
        // 加载JDBC驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 建立db连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mybatis-zq?useUnicode=true&characterEncoding=UTF8&useSSL=false", "root", "root");
        return connection;
    }

    @SneakyThrows
    private void setParam(PreparedStatement ps, Object parameter, List<String> parameterMappings) {
        // 设置值
        Map<Class, TypeHandler> typeHandlerMap = this.configuration.getTypeHandlerMap();
        Map<String, Object> paramValueMap = (Map<String, Object>) parameter;
        for (int i = 0; i < parameterMappings.size(); i++) {
            String jdbcColumnName = parameterMappings.get(i);
            if (jdbcColumnName.contains(".")) {
                String[] split = jdbcColumnName.split("\\.");
                String key = split[0];
                Object instanceValue = paramValueMap.get(key);
                Object fieldValue = ReflectUtil.getFieldValue(instanceValue, split[1]);
                typeHandlerMap.get(fieldValue.getClass()).setParameter(ps, i + 1, fieldValue);
            } else {
                Object val = paramValueMap.get(jdbcColumnName);
                typeHandlerMap.get(val.getClass()).setParameter(ps, i + 1, val);
            }
        }
    }

    @SneakyThrows
    private <T> List<T> handleResultSets(MappedStatement ms, PreparedStatement ps) {
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
```