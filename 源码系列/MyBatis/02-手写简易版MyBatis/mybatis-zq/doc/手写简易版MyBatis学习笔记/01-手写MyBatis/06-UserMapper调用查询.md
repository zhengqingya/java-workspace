# UserMapper调用查询

#### 1、定义查询注解

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Select {
    String value();
}
```

#### 2、实体类

```java
import lombok.Data;

@Data
public class User {
    private Integer id;
    private String name;
    private Integer age;
}
```

#### 3、xxxMapper

```java
import com.zhengqing.demo.entity.User;
import com.zhengqing.mybatis.annotations.Select;

import java.util.List;

public interface UserMapper {

    @Select("select * from t_user")
    List<User> selectList();

}
```

#### 4、模拟正常mapper调用执行数据查询

```java
import com.zhengqing.demo.entity.User;
import com.zhengqing.demo.mapper.UserMapper;
import org.junit.Test;

import java.util.List;

public class TestApp {

    @Test
    public void test() throws Exception {
        UserMapper userMapper = UserMapper.class.newInstance();

        List<User> userList = userMapper.selectList();

        System.out.println(userList);
    }

}
```