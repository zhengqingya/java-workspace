# spring事务管理-01

通过 `DataSourceUtils.getConnection(this.dataSource);` 拿到spring的数据库连接

```java
import com.zhengqing.mybatis.transaction.Transaction;
import lombok.SneakyThrows;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;

public class SpringManagedTransaction implements Transaction {

    private DataSource dataSource;
    private Connection connection;
    private boolean autoCommit;

    public SpringManagedTransaction(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @SneakyThrows
    @Override
    public Connection getConnection() {
        Connection connection = DataSourceUtils.getConnection(this.dataSource);
        this.connection = connection;
        this.autoCommit = connection.getAutoCommit();
        return connection;
    }

    @SneakyThrows
    @Override
    public void commit() {
        if (this.autoCommit) {
            return;
        }
        if (this.connection != null) {
            this.connection.commit();
        }
    }

    @SneakyThrows
    @Override
    public void rollback() {
        if (this.autoCommit) {
            return;
        }
        if (this.connection != null) {
            this.connection.rollback();
        }
    }

    @SneakyThrows
    @Override
    public void close() {
        if (this.connection != null) {
            this.connection.close();
        }
    }
}
```