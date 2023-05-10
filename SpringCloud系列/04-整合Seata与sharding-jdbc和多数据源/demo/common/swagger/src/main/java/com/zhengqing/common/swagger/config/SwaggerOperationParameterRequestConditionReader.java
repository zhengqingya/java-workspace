package com.zhengqing.common.swagger.config;

import com.fasterxml.classmate.TypeResolver;
import com.zhengqing.common.swagger.constant.SwaggerConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spring.web.readers.operation.AbstractOperationParameterRequestConditionReader;
import springfox.documentation.spring.wrapper.NameValueExpression;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.util.Set;

/**
 * <p>
 * swagger 全局参数
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/12/28 23:10
 */
@Primary
@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 10000)
public class SwaggerOperationParameterRequestConditionReader extends AbstractOperationParameterRequestConditionReader {

    private final TypeResolver resolver;

    private final String IPAGE_CLASS_NAME = "com.baomidou.mybatisplus.core.metadata.IPage";

    @Autowired
    public SwaggerOperationParameterRequestConditionReader(TypeResolver resolver) {
        super(resolver);
        this.resolver = resolver;
    }

    @Override
    public void apply(OperationContext context) {
        Set<NameValueExpression<String>> headers = context.headers();
        Set<RequestParameter> requestParameters = this.getRequestParameters(headers, ParameterType.HEADER);
        /**
         * 分页列表api添加分页请求头参数
         * {@link com.zhengqing.common.base.model.dto.BasePageDTO}
         */
        if (this.IPAGE_CLASS_NAME.equals(context.getReturnType().getErasedType().getName())) {
            requestParameters.add(
                    new RequestParameterBuilder()
                            .name(SwaggerConstant.PAGE_NUM)
                            .description("当前页")
                            .in(ParameterType.HEADER)
                            .required(true)
                            .build()
            );
            requestParameters.add(
                    new RequestParameterBuilder()
                            .name(SwaggerConstant.PAGE_SIZE)
                            .description("每页显示数量")
                            .in(ParameterType.HEADER)
                            .required(true)
                            .build());
            context.operationBuilder().requestParameters(requestParameters);
        }
    }

}
