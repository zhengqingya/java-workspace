# 拿到mapper返回类型值

```java
public interface UserMapper {
    @Select("select * from t_user where id = #{id} and name = #{name}")
    List<User> selectList(@Param("id") Integer id, @Param("name") String name);

    @Select("select * from t_user where id = #{id}")
    User selectOne(@Param("id") Integer id);
}
```

```java
public class MapperProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // ...
        ps.execute();

        // 拿到mapper的返回类型
        Class returnType = null;
        Type genericReturnType = method.getGenericReturnType();
        if (genericReturnType instanceof ParameterizedType) {
            returnType = (Class) ((ParameterizedType) genericReturnType).getActualTypeArguments()[0];
        } else if (genericReturnType instanceof Class) {
            returnType = (Class) genericReturnType;
        }

        // 拿到结果集 ...
    }
}
```
