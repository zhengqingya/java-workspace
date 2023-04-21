package com.zhengqing.demo.config.retrunvalue;

import com.google.common.collect.Lists;
import org.springframework.core.MethodParameter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 封装返回值处理
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/8/1 18:40
 */
public class MyHandlerMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

    private final HandlerMethodReturnValueHandler returnValueHandler;

    public MyHandlerMethodReturnValueHandler(HandlerMethodReturnValueHandler returnValueHandler) {
        this.returnValueHandler = returnValueHandler;
    }

    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        return this.returnValueHandler.supportsReturnType(methodParameter);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        String servletPath = webRequest.getNativeRequest(HttpServletRequest.class).getServletPath();
        List<String> excludeUrlList = Lists.newArrayList(
                "/wx/callback/*"
        );
        boolean isHandleReturnValue = true;
        PathMatcher pathMatcher = new AntPathMatcher();
        for (String url : excludeUrlList) {
            if (pathMatcher.match(url, servletPath)) {
                isHandleReturnValue = false;
                break;
            }
        }
        this.returnValueHandler.handleReturnValue(
                isHandleReturnValue ?
                        new HashMap<String, Object>(5) {{
                            this.put("code", 200);
                            this.put("msg", "OK");
                            this.put("data", returnValue);
                        }}
                        : returnValue,
                returnType, mavContainer, webRequest
        );
    }

}
