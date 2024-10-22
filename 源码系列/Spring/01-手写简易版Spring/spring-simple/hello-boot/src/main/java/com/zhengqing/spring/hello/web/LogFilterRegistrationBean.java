package com.zhengqing.spring.hello.web;

import com.zhengqing.spring.annotation.Component;
import com.zhengqing.spring.annotation.Order;
import com.zhengqing.spring.webmvc.FilterRegistrationBean;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Order(100)
@Component
public class LogFilterRegistrationBean extends FilterRegistrationBean {

    @Override
    public List<String> getUrlPatterns() {
        return List.of("/*");
    }

    @Override
    public Filter getFilter() {
        return new LogFilter();
    }
}

@Slf4j
class LogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        log.info("{}: {}", req.getMethod(), req.getRequestURI());
        chain.doFilter(request, response);
    }
}