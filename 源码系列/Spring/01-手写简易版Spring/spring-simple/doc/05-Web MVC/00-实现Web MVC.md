# 实现Web MVC

现在，我们已经实现了IoC容器、AOP、JdbcTemplate和声明式事务，离一个完整的框架只差一个Web MVC了。

我们先看看Spring的Web MVC主要提供了哪些组件和API支持：

1. 一个`DispatcherServlet`作为核心处理组件，接收所有URL请求，然后按MVC规则转发；
2. 基于`@Controller`注解的URL控制器，由应用程序提供，Spring负责解析规则；
3. 提供`ViewResolver`，将应用程序的Controller处理后的结果进行渲染，给浏览器返回页面；
4. 基于`@RestController`注解的REST处理机制，由应用程序提供，Spring负责将输入输出变为JSON格式；
5. 多种拦截器和异常处理器等。
