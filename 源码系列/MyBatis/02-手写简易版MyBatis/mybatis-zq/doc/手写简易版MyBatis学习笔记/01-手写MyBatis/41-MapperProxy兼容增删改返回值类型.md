# MapperProxy兼容增删改返回值类型

返回值类型可能是Long、Integer、void... 需要做兼容

```java
public interface UserMapper {
    @Insert("insert into t_user(name, age) values(#{user.name}, #{user.age})")
    Long insert(@Param("user") User user);

    @Delete("delete from t_user where id = #{id}")
    Long delete(@Param("id") Integer id);
}
```

```java
public class MapperProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // ...
        SqlCommandType sqlCommandType = ms.getSqlCommandType();
        switch (sqlCommandType) {
            case INSERT:
                return this.convertResult(ms, this.sqlSession.insert(statementId, paramValueMap));
            case DELETE:
                return this.convertResult(ms, this.sqlSession.delete(statementId, paramValueMap));
            case UPDATE:
                return this.convertResult(ms, this.sqlSession.update(statementId, paramValueMap));
            case SELECT:
                if (ms.getIsSelectMany()) {
                    return this.sqlSession.selectList(statementId, paramValueMap);
                } else {
                    return this.sqlSession.selectOne(statementId, paramValueMap);
                }
            default:
                break;
        }
        return null;
    }

    private Object convertResult(MappedStatement ms, int updateCount) {
        Class returnType = ms.getReturnType();
        if (returnType == int.class || returnType == Integer.class) {
            return updateCount;
        } else if (returnType == Long.class) {
            return Long.valueOf(updateCount);
        } else if (returnType == void.class) {
            return null;
        }
        return null;
    }
}
```