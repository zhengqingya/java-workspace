package com.zhengqing.common.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * <p>
 * Servlet初始化执行
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2019/8/19 23:08
 */
@Slf4j
@Configuration
public class ServletInit implements ServletContextInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        this.initProperties();
//        loadAllUrlMapping(servletContext);
    }

    /**
     * 初始化
     */
    private void initProperties() {
        log.info("============= ↓↓↓↓↓↓ [初始化系统配置参数] ↓↓↓↓↓↓ =============");
    }

    /**
     * 读取系统URL映射
     *
     * @param servletContext
     */
//    private void loadAllUrlMapping(ServletContext servletContext) {
//        log.info("============= ↓↓↓↓↓↓ [读取系统URL映射] ↓↓↓↓↓↓ =============");
//        ApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
//        Map<String, HandlerMapping> requestMappings = BeanFactoryUtils.beansOfTypeIncludingAncestors(webApplicationContext, HandlerMapping.class, true, false);
//        for (HandlerMapping handlerMapping : requestMappings.values()) {
//            if (handlerMapping instanceof RequestMappingHandlerMapping) {
//                RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping) handlerMapping;
//                Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
//                for (RequestMappingInfo rmi : handlerMethods.keySet()) {
//                    PatternsRequestCondition prc = rmi.getPatternsCondition();
//                    HandlerMethod handlerMethod = handlerMethods.get(rmi);
//                    Set<String> patterns = prc.getPatterns();
//                    for (String uStr : patterns) {
//                        Method methodItem = handlerMethod.getMethod();
//                        ApiOperation apiOperation = methodItem.getAnnotation(ApiOperation.class);
//                        if (apiOperation != null) {
//                            String apiOperationValue = apiOperation.value();
//                            Api annotation = methodItem.getDeclaringClass().getAnnotation(Api.class);
//                            String apiTags = annotation.tags()[0];
//                            log.debug("名称：[{}]  URI：[{}]  Controller方法：[{}]",
//                                    apiTags + "-" + apiOperationValue,
//                                    uStr,
//                                    methodItem.getDeclaringClass().getSimpleName() + "." + methodItem.getName());
//                        }
//                    }
//                }
//            }
//        }
//    }

}
