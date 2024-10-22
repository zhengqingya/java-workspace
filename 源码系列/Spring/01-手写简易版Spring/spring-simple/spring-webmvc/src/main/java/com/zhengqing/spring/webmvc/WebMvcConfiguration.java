package com.zhengqing.spring.webmvc;

import com.zhengqing.spring.annotation.Autowired;
import com.zhengqing.spring.annotation.Bean;
import com.zhengqing.spring.annotation.Configuration;
import com.zhengqing.spring.annotation.Value;
import jakarta.servlet.ServletContext;

import java.util.Objects;

@Configuration
public class WebMvcConfiguration {

    private static ServletContext servletContext = null;

    /**
     * Set by web listener.
     */
    public static void setServletContext(ServletContext ctx) {
        servletContext = ctx;
    }

    @Bean(initMethod = "init")
    ViewResolver viewResolver( //
                               @Autowired ServletContext servletContext,
                               @Value("${spring.web.freemarker.template-path:/WEB-INF/templates}") String templatePath,
                               @Value("${spring.web.freemarker.template-encoding:UTF-8}") String templateEncoding) {
        return new FreeMarkerViewResolver(servletContext, templatePath, templateEncoding);
    }

    @Bean
    ServletContext servletContext() {
        return Objects.requireNonNull(servletContext, "ServletContext is not set.");
    }
}
