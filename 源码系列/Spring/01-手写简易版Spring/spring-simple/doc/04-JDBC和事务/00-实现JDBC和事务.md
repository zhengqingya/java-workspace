# 实现JDBC和事务

我们已经实现了IoC容器和AOP功能，在此基础上增加JDBC和事务的支持就比较容易了。

Spring对JDBC数据库的支持主要包括：

1. 提供了一个`JdbcTemplate`和`NamedParameterJdbcTemplate`模板类，可以方便地操作JDBC；
2. 支持流行的ORM框架，如Hibernate、JPA等；
3. 支持声明式事务，只需要通过简单的注解配置即可实现事务管理。
