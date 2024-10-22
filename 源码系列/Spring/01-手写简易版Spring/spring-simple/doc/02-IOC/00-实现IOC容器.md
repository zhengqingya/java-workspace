# 实现IOC容器

- 解耦合：通过IOC容器管理对象的创建和依赖关系，降低了组件之间的耦合度。
- 统一管理：所有Bean的生命周期、配置参数都在Spring配置文件或注解中定义，方便维护和管理。
- 自动装配：Spring可以根据配置自动将Bean实例化，并且根据依赖关系自动注入相应的对象，减少了代码量。

### Annotation配置

从使用者的角度看，使用IoC容器时，需要定义一个入口配置，它通常长这样：

```java
@ComponentScan
public class AppConfig {  }
```

`AppConfig`配置类：通过`@ComponentScan`来标识要扫描的Bean的包。

1. 如果没有明确写出包名，那么将基于`AppConfig`所在包进行扫描
2. 如果明确写出了包名，则在指定的包下进行扫描

在扫描过程中，凡是带有注解`@Component`的类，将被添加到IoC容器进行管理：

```java
@Component
public class Hello {  }
```

我们用到的许多第三方组件也经常会纳入IoC容器管理。这些第三方组件是不可能带有`@Component`注解的，引入第三方Bean只能通过工厂模式，即在`@Configuration`工厂类中定义带`@Bean`的工厂方法：

```java
@Configuration
public class DbConfig {
    @Bean
    DataSource createDataSource(...) {
        return new HikariDataSource(...);
    }

    @Bean
    JdbcTemplate createJdbcTemplate(...) {
        return new JdbcTemplate(...);
    }
}
```

