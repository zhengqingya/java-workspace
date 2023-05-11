package com.zhengqing.common.web.config;

import com.zhengqing.common.web.handler.MyHandlerMethodReturnValueHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 返回值配置
 * </p>
 *
 * @author zhengqingya
 * @description 用定制的Handler替换默认Handler
 * @date 2020/8/1 18:42
 */
@Configuration
public class ReturnValueConfig implements InitializingBean {

    @Resource
    RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<HandlerMethodReturnValueHandler> unmodifiableList = this.requestMappingHandlerAdapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> list = new ArrayList<>(unmodifiableList.size());
        for (HandlerMethodReturnValueHandler returnValueHandler : unmodifiableList) {
            if (returnValueHandler instanceof RequestResponseBodyMethodProcessor) {
                list.add(new MyHandlerMethodReturnValueHandler(returnValueHandler));
            } else {
                list.add(returnValueHandler);
            }
        }
        this.requestMappingHandlerAdapter.setReturnValueHandlers(list);
    }

}
