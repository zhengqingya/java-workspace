package com.zhengqing.demo.config;

import cn.hutool.core.util.StrUtil;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Web MVC 日志上下文配置。
 */
@Configuration
public class WebMvcOtelLogConfig implements WebMvcConfigurer {

    private static final String MDC_HTTP_REQUEST_METHOD = "http.request.method";
    private static final String MDC_HTTP_ROUTE = "http.route";
    private static final String UNKNOWN_ROUTE = "unknown";

    /**
     * 注册请求日志上下文拦截器。
     *
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1、注册全局请求拦截器，为后续 ERROR 日志补充接口维度。
        registry.addInterceptor(new OtelLogContextInterceptor()).addPathPatterns("/**");
    }

    /**
     * OTel 日志上下文拦截器。
     */
    private static class OtelLogContextInterceptor implements HandlerInterceptor {

        /**
         * 请求进入业务处理前写入日志上下文。
         *
         * @param request  HTTP 请求
         * @param response HTTP 响应
         * @param handler  当前处理器
         * @return 是否继续执行后续处理
         */
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            // 1、写入请求方法，便于 Loki/飞书告警展示 GET、POST 等接口动作。
            MDC.put(MDC_HTTP_REQUEST_METHOD, request.getMethod());

            // 2、优先写入 Spring MVC 匹配后的路由模板，避免把路径参数展开成高基数字段。
            MDC.put(MDC_HTTP_ROUTE, this.getHttpRoute(request));

            // 3、继续执行后续业务处理。
            return true;
        }

        /**
         * 请求完成后清理日志上下文。
         *
         * @param request  HTTP 请求
         * @param response HTTP 响应
         * @param handler  当前处理器
         * @param ex       处理异常
         */
        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
            // 1、清理请求方法，避免线程复用时串到下一次请求。
            MDC.remove(MDC_HTTP_REQUEST_METHOD);

            // 2、清理请求路由，避免线程复用时串到下一次请求。
            MDC.remove(MDC_HTTP_ROUTE);
        }

        /**
         * 获取当前请求的接口路由。
         *
         * @param request HTTP 请求
         * @return Spring MVC 路由模板或兜底路径
         */
        private String getHttpRoute(HttpServletRequest request) {
            // 1、读取 Spring MVC 匹配后的路由模板，例如 /error、/hello、/order/{id}。
            Object bestMatchingPattern = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
            String route = bestMatchingPattern == null ? null : bestMatchingPattern.toString();

            // 2、路由模板为空时使用请求路径兜底，确保飞书消息不再显示未识别。
            route = StrUtil.blankToDefault(route, request.getRequestURI());

            // 3、统一补齐前导斜杠，并对极端空值返回固定 unknown。
            route = StrUtil.blankToDefault(route, UNKNOWN_ROUTE);
            if (UNKNOWN_ROUTE.equals(route) || StrUtil.startWith(route, "/")) {
                return route;
            }
            return "/" + route;
        }

    }

}
