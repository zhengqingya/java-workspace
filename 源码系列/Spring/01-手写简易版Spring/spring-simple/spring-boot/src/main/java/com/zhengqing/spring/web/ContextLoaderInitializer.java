package com.zhengqing.spring.web;

import com.zhengqing.spring.context.AnnotationConfigApplicationContext;
import com.zhengqing.spring.context.ApplicationContext;
import com.zhengqing.spring.io.PropertyResolver;
import com.zhengqing.spring.webmvc.WebMvcConfiguration;
import com.zhengqing.spring.webmvc.utils.WebUtils;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class ContextLoaderInitializer implements ServletContainerInitializer {

    final Class<?> configClass;
    final PropertyResolver propertyResolver;

    public ContextLoaderInitializer(Class<?> configClass, PropertyResolver propertyResolver) {
        this.configClass = configClass;
        this.propertyResolver = propertyResolver;
    }

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        log.info("Servlet container start. ServletContext = {}", ctx);

        String encoding = propertyResolver.getProperty("${summer.web.character-encoding:UTF-8}");
        ctx.setRequestCharacterEncoding(encoding);
        ctx.setResponseCharacterEncoding(encoding);

        // 设置ServletContext
        WebMvcConfiguration.setServletContext(ctx);
        // 启动IoC容器
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(this.configClass, this.propertyResolver);
        log.info("Application context created: {}", applicationContext);

        // 注册Filter与DispatcherServlet
        WebUtils.registerFilters(ctx);
        WebUtils.registerDispatcherServlet(ctx, this.propertyResolver);
    }
}
