# ResultSet结果映射

在MyBatis中，`ResultSet`结果映射是一个关键过程，它负责将数据库查询结果（`ResultSet`）映射到Java对象。
这个过程确保从数据库中获取的数据能够正确地转换为应用程序所需的Java对象。

### 代码实现

#### 1、字段类型处理器中新增获取值的方法 `getResult`

```java
public interface TypeHandler<T> {
    /**
     * 设置值
     *
     * @param ps        PreparedStatement
     * @param i         参数位置
     * @param parameter 参数值
     * @return void
     * @author zhengqingya
     * @date 2024/4/22 16:19
     */
    void setParameter(PreparedStatement ps, int i, T parameter) throws SQLException;

    /**
     * 获取值
     *
     * @param rs         ResultSet结果集
     * @param columnName 字段名称
     * @return 字段值
     * @author zhengqingya
     * @date 2024/4/22 16:19
     */
    T getResult(ResultSet rs, String columnName) throws SQLException;
}
```

Integer字段类型处理器

```java
public class IntegerTypeHandler implements TypeHandler<Integer> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Integer parameter) throws SQLException {
        ps.setInt(i, parameter);
    }

    @Override
    public Integer getResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getInt(columnName);
    }
}
```

String字段类型处理器

```java
public class StringTypeHandler implements TypeHandler<String> {
    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter) throws SQLException {
        ps.setString(i, parameter);
    }

    @Override
    public String getResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }
}
```

#### 2、MapperProxy修改结果映射代码

```java
public class MapperProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // ...
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
                Object val = this.typeHandlerMap.get(field.getType()).getResult(rs, columnName);
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
}
```

#### 3、测试类

```java
import cn.hutool.json.JSONUtil;
import com.zhengqing.demo.entity.User;
import com.zhengqing.demo.mapper.UserMapper;
import com.zhengqing.mybatis.binding.MapperProxyFactory;
import org.junit.Test;

import java.util.List;

public class TestApp {
    @Test
    public void test() throws Exception {
        UserMapper userMapper = MapperProxyFactory.getProxy(UserMapper.class);
        List<User> userList = userMapper.selectList(1, "zq");
        System.out.println(JSONUtil.toJsonStr(userList));
    }
}
```