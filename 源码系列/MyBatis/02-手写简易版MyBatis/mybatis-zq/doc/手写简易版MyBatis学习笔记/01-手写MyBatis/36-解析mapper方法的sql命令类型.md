# 解析mapper方法的sql命令类型

#### 1、SQL命令类型

```java
public enum SqlCommandType {
    INSERT, DELETE, UPDATE, SELECT;
}
```

#### 2、mapper配置信息新增SQL命令类型 `SqlCommandType sqlCommandType`

```java
public class MappedStatement {
    private String id; // 唯一标识 eg: com.zhengqing.demo.mapper.UserMapper.selectList
    private String sql; // SQL
    private Class returnType;// 返回类型
    private SqlCommandType sqlCommandType; // SQL命令类型
}
```

#### 3、XML配置构建器 解析sql命令类型

```java
public class XMLConfigBuilder {

    private List<Class<? extends Annotation>> sqlAnnotationTypeList = Lists.newArrayList(Insert.class, Delete.class, Update.class, Select.class);

    public Configuration parse() {
        Configuration configuration = new Configuration();
        // 解析mapper
        this.parseMapper(configuration);
        return configuration;
    }

    @SneakyThrows
    private void parseMapper(Configuration configuration) {
        Set<Class<?>> classes = ClassUtil.scanPackage("com.zhengqing.demo.mapper");
        for (Class<?> aClass : classes) {
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                SqlCommandType sqlCommandType = null;
                String originalSql = ""; // 原始sql
                for (Class<? extends Annotation> sqlAnnotationType : this.sqlAnnotationTypeList) {
                    Annotation annotation = method.getAnnotation(sqlAnnotationType);
                    if (annotation != null) {
                        originalSql = (String) annotation.getClass().getMethod("value").invoke(annotation);
                        if (annotation instanceof Insert) {
                            sqlCommandType = SqlCommandType.INSERT;
                        } else if (annotation instanceof Delete) {
                            sqlCommandType = SqlCommandType.DELETE;
                        } else if (annotation instanceof Update) {
                            sqlCommandType = SqlCommandType.UPDATE;
                        } else if (annotation instanceof Select) {
                            sqlCommandType = SqlCommandType.SELECT;
                        }
                        break;
                    }
                }

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
                        .sqlCommandType(sqlCommandType)
                        .build();
                configuration.addMappedStatement(mappedStatement);
            }
        }
    }
}
```
