# mapper配置解析

作用：项目启动时解析mapper配置，构建mappedStatement信息，保存到Configuration中。
避免每次执行sql时都去解析mapper信息。

#### 1、mapper配置信息

```java
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MappedStatement {
    private String id; // 唯一标识 eg: com.zhengqing.demo.mapper.UserMapper.selectList
    private String sql; // SQL
    private Class returnType;// 返回类型
}
```

#### 2、核心配置

```java
import com.zhengqing.mybatis.mapping.MappedStatement;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Configuration {

    // eg: com.zhengqing.demo.mapper.UserMapper.selectList --> mapper配置信息
    private Map<String, MappedStatement> mappedStatements = new HashMap<>();

    public void addMappedStatement(MappedStatement ms) {
        this.mappedStatements.put(ms.getId(), ms);
    }

    public MappedStatement getMappedStatement(String id) {
        return this.mappedStatements.get(id);
    }
}
```

#### 3、XML配置构建器

> 根据包名扫描mapper配置

```java
import cn.hutool.core.util.ClassUtil;
import com.zhengqing.mybatis.annotations.Select;
import com.zhengqing.mybatis.mapping.MappedStatement;
import com.zhengqing.mybatis.session.Configuration;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

public class XMLConfigBuilder {

    public Configuration parse() {
        Configuration configuration = new Configuration();
        // 解析mapper
        this.parseMapper(configuration);
        return configuration;
    }

    private void parseMapper(Configuration configuration) {
        Set<Class<?>> classes = ClassUtil.scanPackage("com.zhengqing.demo.mapper");
        for (Class<?> aClass : classes) {
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                // 拿到sql
                Select select = method.getAnnotation(Select.class);
                String originalSql = select.value(); // 原始sql

                // 拿到mapper的返回类型
                Class returnType = null;
                Type genericReturnType = method.getGenericReturnType();
                if (genericReturnType instanceof ParameterizedType) {
                    returnType = (Class) ((ParameterizedType) genericReturnType).getActualTypeArguments()[0];
                } else if (genericReturnType instanceof Class) {
                    returnType = (Class) genericReturnType;
                }

                // 封装
                MappedStatement mappedStatement = MappedStatement.builder()
                        .id(aClass.getName() + "." + method.getName())
                        .sql(originalSql)
                        .returnType(returnType)
                        .build();
                configuration.addMappedStatement(mappedStatement);
            }
        }
    }
}
```

#### 4、测试

```java
import com.zhengqing.demo.entity.User;
import com.zhengqing.mybatis.annotations.Param;
import com.zhengqing.mybatis.annotations.Select;

public interface TestMapper {
    @Select("select * from t_user where id = #{id}")
    User selectOne(@Param("id") Integer id);
}
```

运行后debug查看解析结果

```java
public class TestApp {
    @Test
    public void test() throws Exception {
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        xmlConfigBuilder.parse();
    }
}
```
