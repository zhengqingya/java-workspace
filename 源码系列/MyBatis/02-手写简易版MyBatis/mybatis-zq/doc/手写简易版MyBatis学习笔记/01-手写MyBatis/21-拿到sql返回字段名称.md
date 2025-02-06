# 拿到sql返回字段名称

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
//            System.out.println(rs.getString("name") + " -- " + rs.getInt("age"));
            Object instance = returnType.newInstance();

            ReflectUtil.setFieldValue(instance, "", null);

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