# SqlSession工厂构建者

SqlSessionFactoryBuilder用于根据配置文件或配置对象构建SqlSessionFactory。
构建完成后，SqlSessionFactoryBuilder可以被丢弃，因为它不再需要。

```java
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build() {
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parse();
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        return sqlSessionFactory;
    }

}
```

测试类

```java
public class TestApp {

    @Test
    public void test() throws Exception {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        List<User> userList = userMapper.selectList(1, "zq");
        System.out.println(JSONUtil.toJsonStr(userList));

//        System.out.println(userMapper.selectOne(1));
    }

}
```
