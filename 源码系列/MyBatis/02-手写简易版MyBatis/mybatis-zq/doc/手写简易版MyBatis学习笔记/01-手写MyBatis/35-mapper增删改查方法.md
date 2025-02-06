# mapper增删改查方法

#### 1、定义增删改查注解

```java

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Insert {
    String value();
}
```

```java

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Delete {
    String value();
}
```

```java

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Update {
    String value();
}
```

```java

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Select {
    String value();
}
```

#### 2、定义mapper增删改查接口

```java
public interface UserMapper {
    @Select("select * from t_user where id = #{id} and name = #{name}")
    List<User> selectList(@Param("id") Integer id, @Param("name") String name);

    @Select("select * from t_user where id = #{id}")
    User selectOne(@Param("id") Integer id);

    @Insert("insert into t_user(id, name, age) values(#{user.id}, #{user.name}, #{user.age})")
    Integer insert(@Param("user") User user);

    @Delete("delete from t_user where id = #{id}")
    Integer delete(@Param("id") Integer id);

    @Update("update t_user set name = #{name} where id = #{id}")
    Integer update(@Param("id") Integer id, @Param("name") String name);
}
```