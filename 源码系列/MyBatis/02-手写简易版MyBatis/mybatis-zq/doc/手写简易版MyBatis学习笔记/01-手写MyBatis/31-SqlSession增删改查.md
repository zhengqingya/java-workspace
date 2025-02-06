# SqlSession增删改查

`SqlSession`是MyBatis的核心接口之一，它提供了执行SQL命令、管理事务以及与Mapper接口交互的功能。
以下是`SqlSession`的主要作用和功能：

### 主要作用

1. **执行SQL命令**：
    - 通过`SqlSession`可以执行SQL语句，包括`SELECT`、`INSERT`、`UPDATE`和`DELETE`等操作。
    - 可以直接调用`SqlSession`的方法来执行SQL语句，例如`selectOne`、`selectList`、`insert`、`update`和`delete`。
2. **管理事务**：
    - `SqlSession`负责管理事务的开启、提交和回滚。
    - 可以通过`SqlSession`的`commit`和`rollback`方法来控制事务。
3. **获取Mapper接口**：
    - 通过`SqlSession`可以获取Mapper接口的代理对象，从而调用Mapper接口中的方法。
    - 使用`SqlSession`的`getMapper`方法可以获取Mapper接口的实例。
4. **缓存管理**：
    - `SqlSession`管理一级缓存（本地缓存），确保在同一个`SqlSession`中多次查询相同的数据时，不会重复访问数据库。
    - 一级缓存默认在`SqlSession`的生命周期内有效。
5. **资源管理**：
    - `SqlSession`负责管理数据库连接和其他资源，确保资源的正确释放。
    - 在使用完`SqlSession`后，应该调用`close`方法来关闭它，释放相关资源。

### 代码实现

定义SqlSession增删改查接口

```java
import java.util.List;

public interface SqlSession {
    int insert(String statementId, Object parameter);

    int delete(String statementId, Object parameter);

    int update(String statementId, Object parameter);

    <T> T selectOne(String statementId, Object parameter);

    <T> List<T> selectList(String statementId, Object parameter);
}
```

默认SqlSession

```java
import com.zhengqing.mybatis.executor.Executor;
import com.zhengqing.mybatis.mapping.MappedStatement;

import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;
    private Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public int insert(String statementId, Object parameter) {
        MappedStatement ms = this.configuration.getMappedStatement(statementId);
        return this.executor.update(ms, parameter);
    }

    @Override
    public int delete(String statementId, Object parameter) {
        MappedStatement ms = this.configuration.getMappedStatement(statementId);
        return this.executor.update(ms, parameter);
    }

    @Override
    public int update(String statementId, Object parameter) {
        MappedStatement ms = this.configuration.getMappedStatement(statementId);
        return this.executor.update(ms, parameter);
    }

    @Override
    public <T> T selectOne(String statementId, Object parameter) {
        List<T> list = this.selectList(statementId, parameter);
        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() > 1) {
            throw new RuntimeException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    @Override
    public <T> List<T> selectList(String statementId, Object parameter) {
        MappedStatement ms = this.configuration.getMappedStatement(statementId);
        return this.executor.query(ms, parameter);
    }
}
```