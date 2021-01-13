package com.zhengqing.demo.custom;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.fastjson.JSONObject;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 编写参数解析器
 * </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2021/1/13 14:41
 */
@Slf4j
public class RequestPostSingleParamMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String POST = "post";
    private static final String APPLICATION_JSON = "application/json";

    /**
     * 判断是否需要处理该参数
     *
     * @param parameter
     *            the method parameter to check
     * @return {@code true} if this resolver supports the supplied parameter; {@code false} otherwise
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 只处理带有@RequestPostSingleParam注解的参数
        return parameter.hasParameterAnnotation(RequestPostSingleParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String contentType = Objects.requireNonNull(servletRequest).getContentType();

        if (contentType == null || !contentType.contains(APPLICATION_JSON)) {
            log.error("《RequestPostSingleParam》 contentType需为【{}】", APPLICATION_JSON);
            throw new RuntimeException("《RequestPostSingleParam》 contentType需为application/json");
        }

        if (!POST.equalsIgnoreCase(servletRequest.getMethod())) {
            log.error("《RequestPostSingleParam》 请求类型必须为post");
            throw new RuntimeException("《RequestPostSingleParam》 请求类型必须为post");
        }
        return this.bindRequestParams(parameter, servletRequest);
    }

    private Object bindRequestParams(MethodParameter parameter, HttpServletRequest servletRequest) {
        RequestPostSingleParam requestPostSingleParam = parameter.getParameterAnnotation(RequestPostSingleParam.class);
        Class<?> parameterType = parameter.getParameterType();
        String requestBody = this.getRequestBody(servletRequest);
        Map paramObj = JSONObject.parseObject(requestBody, Map.class);
        if (paramObj == null) {
            paramObj = new JSONObject();
        }
        // if (paramObj.size() > 1) {
        // throw new RuntimeException("《RequestPostSingleParam》 post请求只支持接收单个参数!");
        // }

        String parameterName = StringUtils.isBlank(requestPostSingleParam.value()) ? parameter.getParameterName()
            : requestPostSingleParam.value();
        Object value = paramObj.get(parameterName);

        if (requestPostSingleParam.required()) {
            if (value == null) {
                log.error("《RequestPostSingleParam》 require=true,参数【{}】不能为空!", parameterName);
                throw new RuntimeException("《RequestPostSingleParam》 " + parameterName + "不能为空!");
            }
        }

        return ConvertUtils.convert(value, parameterType);
    }

    /**
     * 获取请求body
     *
     * @param servletRequest:
     *            request
     * @return: 请求body
     */
    private String getRequestBody(HttpServletRequest servletRequest) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = servletRequest.getReader();
            char[] buf = new char[1024];
            int length;
            while ((length = reader.read(buf)) != -1) {
                stringBuilder.append(buf, 0, length);
            }
        } catch (IOException e) {
            log.error("《RequestPostSingleParam》 读取流异常", e);
            throw new RuntimeException("《RequestPostSingleParam》 读取流异常");
        }
        return stringBuilder.toString();
    }

}
