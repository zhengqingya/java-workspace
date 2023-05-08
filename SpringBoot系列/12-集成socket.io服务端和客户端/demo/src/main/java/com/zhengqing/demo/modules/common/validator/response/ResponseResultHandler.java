package com.zhengqing.demo.modules.common.validator.response;

import com.zhengqing.demo.modules.common.dto.output.ApiResult;
import com.zhengqing.demo.modules.common.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 *  <p> 重写返回体 </p>
 *
 * @author：  zhengqing <br/>
 * @date：  2019/12/24$ 18:05$ <br/>
 * @version：  <br/>
 */
@Slf4j
@ControllerAdvice
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {

    // 标记名称
    public static final String RESPONSE_RESULT_ANN = "RESPONSE_RESULT_ANN";

    // 是否请求 包含了 包装注解 标记，没有就直接返回，不需要重写返回体
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = sra.getRequest();
        // 判断请求，是否有包装标记
        ResponseResult responseResultAnn = (ResponseResult) request.getAttribute(RESPONSE_RESULT_ANN);
        return responseResultAnn != null;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        log.info(" 进入 返回体 重写格式 处理中 ... ");

        // 处理方法体中抛出的异常
        if (body instanceof MyException) {
            MyException errorResult = (MyException) body;
            return ApiResult.fail(errorResult.getMessage());
        }

        return ApiResult.ok(body);
    }

}
