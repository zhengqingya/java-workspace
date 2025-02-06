# 工厂模式生产SqlSession

在MyBatis中，工厂模式用于创建SqlSession对象。
SqlSessionFactory是一个工厂接口，作为工厂模式的核心，负责集中管理和配置SqlSession的创建过程，确保应用程序能够高效地与数据库进行交互。

主要概念

1. SqlSessionFactory： SqlSessionFactory是MyBatis的核心接口之一，负责创建SqlSession实例。通常在应用程序启动时通过SqlSessionFactoryBuilder构建。
2. SqlSessionFactoryBuilder：SqlSessionFactoryBuilder用于根据配置文件或配置对象构建SqlSessionFactory。构建完成后，SqlSessionFactoryBuilder可以被丢弃，因为它不再需要。
3. SqlSession：SqlSession是MyBatis的核心接口，用于执行SQL命令、管理事务以及与Mapper接口交互。通过SqlSessionFactory的openSession方法创建。

#### 工厂模式的优势

- 集中管理： 通过SqlSessionFactory集中管理SqlSession的创建过程，便于统一配置和管理。
- 可配置性：可以通过配置文件或配置对象灵活地配置SqlSessionFactory，适应不同的环境和需求。
- 资源管理： SqlSessionFactory负责管理数据库连接和其他资源，确保资源的正确释放。

### 代码实现

#### 1、定义SqlSessionFactory -- 生产SqlSession

```java
public interface SqlSessionFactory {
    SqlSession openSession();
}
```

#### 2、实现默认的SqlSessionFactory

```java
public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(this.configuration, this.configuration.newExecutor());
    }
}
```

#### 3、测试类

```java
public class TestApp {

    @Test
    public void test() throws Exception {
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parse();

        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        List<User> userList = userMapper.selectList(1, "zq");
        System.out.println(JSONUtil.toJsonStr(userList));

//        System.out.println(userMapper.selectOne(1));
    }

}
```
