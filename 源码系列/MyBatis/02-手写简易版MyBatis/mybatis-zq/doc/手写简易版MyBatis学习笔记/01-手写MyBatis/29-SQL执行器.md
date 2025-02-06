# SQL执行器

在MyBatis中，`Executor`（SQL执行器）是一个核心组件，负责执行SQL语句并将结果映射到Java对象。

以下是`Executor`的主要作用和功能：

### 主要作用

1. **执行SQL语句**：
    - 负责执行由Mapper接口方法生成的SQL语句，包括`SELECT`、`INSERT`、`UPDATE`和`DELETE`等操作。
2. **结果映射**：
    - 将执行SQL语句返回的结果集映射到Java对象，根据Mapper接口方法的返回类型进行相应的转换。
3. **缓存管理**：
    - 管理一级缓存（本地缓存）和二级缓存（全局缓存），以提高查询性能。
    - 一级缓存默认在SqlSession范围内有效，二级缓存可以在多个SqlSession之间共享。
4. **事务管理**：
    - 负责事务的开启、提交和回滚操作，确保数据库操作的一致性。
5. **延迟加载**：
    - 支持延迟加载（Lazy Loading），即在需要时才加载关联对象，减少不必要的数据库访问。

### 主要类型

MyBatis提供了几种不同的`Executor`实现，以满足不同的需求：

1. **SimpleExecutor**：
    - 最基本的执行器，每次执行SQL语句都会创建一个新的Statement对象。
    - 不支持缓存，适合简单的场景。
2. **ReuseExecutor**：
    - 重用Statement对象，减少Statement的创建和销毁开销。
    - 也不支持缓存，适合需要重用Statement的场景。
3. **BatchExecutor**：
    - 支持批量操作，可以将多个SQL语句批量执行，提高性能。
    - 适合需要批量插入、更新或删除的场景。
4. **CachingExecutor**：
    - 在其他执行器的基础上增加了缓存功能，支持一级缓存和二级缓存。
    - 是默认的执行器类型，适合大多数场景。

### 代码实现

#### 1、定义SQL执行器

```java
import com.zhengqing.mybatis.mapping.MappedStatement;

import java.util.List;

public interface Executor {
    <T> List<T> query(MappedStatement ms, Object parameter);

    int update(MappedStatement ms, Object parameter);
}
```

#### 2、实现一个简单执行器

```java
import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Lists;
import com.zhengqing.mybatis.mapping.MappedStatement;
import com.zhengqing.mybatis.parsing.GenericTokenParser;
import com.zhengqing.mybatis.parsing.ParameterMappingTokenHandler;
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

        String originalSql = ms.getSql(); // 原始sql

        // sql解析  #{}  --- ?
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        String sql = genericTokenParser.parse(originalSql);
        List<String> parameterMappings = parameterMappingTokenHandler.getParameterMappings();

        // 构建sql & 执行
        PreparedStatement ps = connection.prepareStatement(sql);

        Map<Class, TypeHandler> typeHandlerMap = this.configuration.getTypeHandlerMap();


        // 设置值
        Map<String, Object> paramValueMap = (Map<String, Object>) parameter;
        for (int i = 0; i < parameterMappings.size(); i++) {
            String jdbcColumnName = parameterMappings.get(i);
            Object val = paramValueMap.get(jdbcColumnName);
            typeHandlerMap.get(val.getClass()).setParameter(ps, i + 1, val);
        }

        ps.execute();

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

        // 释放资源
        rs.close();
        ps.close();
        connection.close();
        return list;
    }

    @Override
    public int update(MappedStatement ms, Object parameter) {
        return 0;
    }

    @SneakyThrows
    private static Connection getConnection() {
        // 加载JDBC驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 建立db连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mybatis-zq?useUnicode=true&characterEncoding=UTF8&useSSL=false", "root", "root");
        return connection;
    }
}
```

#### 3、mapper代理修改

```java
import com.google.common.collect.Maps;
import com.zhengqing.mybatis.annotations.Param;
import com.zhengqing.mybatis.executor.Executor;
import com.zhengqing.mybatis.mapping.MappedStatement;
import com.zhengqing.mybatis.session.Configuration;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public class MapperProxy implements InvocationHandler {

    private Configuration configuration;
    private Class mapperClass;

    public MapperProxy(Configuration configuration, Class mapperClass) {
        this.configuration = configuration;
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

        MappedStatement ms = this.configuration.getMappedStatement(this.mapperClass.getName() + "." + method.getName());
        Executor executor = this.configuration.newExecutor();
        return executor.query(ms, paramValueMap);
    }
}
```

#### 4、核心配置配置

```java
import com.google.common.collect.Maps;
import com.zhengqing.mybatis.executor.Executor;
import com.zhengqing.mybatis.executor.SimpleExecutor;
import com.zhengqing.mybatis.mapping.MappedStatement;
import com.zhengqing.mybatis.type.IntegerTypeHandler;
import com.zhengqing.mybatis.type.StringTypeHandler;
import com.zhengqing.mybatis.type.TypeHandler;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Configuration {

    private Map<Class, TypeHandler> typeHandlerMap = Maps.newHashMap();

    // eg: com.zhengqing.demo.mapper.UserMapper.selectList --> mapper配置信息
    private Map<String, MappedStatement> mappedStatements = new HashMap<>();

    public Configuration() {
        this.typeHandlerMap.put(Integer.class, new IntegerTypeHandler());
        this.typeHandlerMap.put(String.class, new StringTypeHandler());
    }

    public void addMappedStatement(MappedStatement ms) {
        this.mappedStatements.put(ms.getId(), ms);
    }

    public MappedStatement getMappedStatement(String id) {
        return this.mappedStatements.get(id);
    }

    public Executor newExecutor() {
        return new SimpleExecutor(this);
    }
}
```