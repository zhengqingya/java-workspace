package com.zhengqing.spring.webmvc;

import com.zhengqing.spring.context.AnnotationConfigApplicationContext;
import com.zhengqing.spring.context.ApplicationContext;
import com.zhengqing.spring.exception.NestedRuntimeException;
import com.zhengqing.spring.io.PropertyResolver;
import com.zhengqing.spring.webmvc.utils.WebUtils;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextLoaderListener implements ServletContextListener {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("init {}.", getClass().getName());
        ServletContext servletContext = sce.getServletContext();
        WebMvcConfiguration.setServletContext(servletContext);

        PropertyResolver propertyResolver = WebUtils.createPropertyResolver();
        String encoding = propertyResolver.getProperty("${summer.web.character-encoding:UTF-8}");
        servletContext.setRequestCharacterEncoding(encoding);
        servletContext.setResponseCharacterEncoding(encoding);
        ApplicationContext applicationContext = createApplicationContext(servletContext.getInitParameter("configuration"), propertyResolver);
        // register filters:
        WebUtils.registerFilters(servletContext);
        // register DispatcherServlet:
        WebUtils.registerDispatcherServlet(servletContext, propertyResolver);

        servletContext.setAttribute("applicationContext", applicationContext);
    }

    ApplicationContext createApplicationContext(String configClassName, PropertyResolver propertyResolver) {
        logger.info("init ApplicationContext by configuration: {}", configClassName);
        if (configClassName == null || configClassName.isEmpty()) {
            throw new NestedRuntimeException("Cannot init ApplicationContext for missing init param name: configuration");
        }
        Class<?> configClass;
        try {
            configClass = Class.forName(configClassName);
        } catch (ClassNotFoundException e) {
            throw new NestedRuntimeException("Could not load class from init param 'configuration': " + configClassName);
        }
        return new AnnotationConfigApplicationContext(configClass, propertyResolver);
    }
}
