package com.zhengqing.common.swagger.config;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p> Knife4j配置类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/6/21 14:01
 */
@Slf4j
@Configuration
//@EnableKnife4j
@EnableSwagger2WebMvc
// 对JSR303提供支持
@Import(BeanValidatorPluginsConfiguration.class)
public class Knife4jConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private String port;

    @Value("${knife4j.passwordTokenUrl}")
    private String passwordTokenUrl;

    @Resource
    private OpenApiExtensionResolver openApiExtensionResolver;

    @Bean
    public Docket defaultApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                .groupName(this.applicationName)
                .select()
                // 添加@Api注解才显示
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
//                .apis(RequestHandlerSelectors.basePackage("com.zhengqing"))
                .paths(PathSelectors.any())
                .build()
                // 插件扩展 -- ex:自定义md文档
                .extensions(this.openApiExtensionResolver.buildExtensions(this.applicationName))
//                // 默认全局参数
//                .globalRequestParameters(
//                        Lists.newArrayList(
//                                new RequestParameterBuilder()
//                                        .name(SwaggerConstant.TENANT_ID)
//                                        .description("租户ID")
//                                        .in(ParameterType.HEADER)
//                                        .required(true)
//                                        .build()
//                        )
//                )
                ;

        // 网关|授权服务开启授权认证请求头
//        if (SwaggerConstant.GATEWAY_PORT.equals(this.port) || SwaggerConstant.AUTH_PORT.equals(this.port)) {
        // 2.0.9版本
        // context
//        List<SecurityContext> securityContexts = Lists.newArrayList(
//                SecurityContext.builder()
//                        .securityReferences(
//                                CollectionUtil.newArrayList(
//                                        new SecurityReference("oauth2",
//                                                Lists.newArrayList(
//                                                        new AuthorizationScope("read", "read  resources"),
//                                                        new AuthorizationScope("write", "write resources"),
//                                                        new AuthorizationScope("reads", "read all resources"),
//                                                        new AuthorizationScope("writes", "write all resources")
//                                                ).toArray(new AuthorizationScope[]{})
//                                        )
//                                )
//                        )
//                        .forPaths(PathSelectors.ant("/**"))
//                        .build()
//        );
//        // 密码模式
//        List<SecurityScheme> securitySchemes = Lists.newArrayList(
//                new OAuthBuilder()
//                        .name("oauth2")
//                        .grantTypes(Lists.newArrayList(new ResourceOwnerPasswordCredentialsGrant(this.passwordTokenUrl)))
//                        .build()
//        );


        // 3.0.3版本  参考 https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/blob/master/knife4j-springfox-boot-v3-demo/src/main/java/com/xiaominfo/knife4j/config/Knife4jConfig.java
//            AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//            AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//            authorizationScopes[0] = authorizationScope;
//
//            List<SecurityContext> securityContexts = Lists.newArrayList(
//                    SecurityContext.builder()
//                            .securityReferences(
//                                    CollectionUtil.newArrayList(
//                                            new SecurityReference(HttpHeaders.AUTHORIZATION, authorizationScopes)
//                                    )
//                            )
//                            .forPaths(PathSelectors.regex("/.*"))
//                            .build());
//
//            List<SecurityScheme> securitySchemes = Lists.newArrayList(
//                    HttpAuthenticationScheme.JWT_BEARER_BUILDER
//                            .name(SwaggerConstant.REQUEST_HEADER_AUTHORIZATION)
//                            .description("Bearer Token")
//                            .build()
//            );

//        docket.securityContexts(securityContexts).securitySchemes(securitySchemes);
//        }
        return docket;
    }

    @Bean
    public Docket systemApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                .groupName("系统模块")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zhengqing.system"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    @Bean
    public Docket wxMpApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                .groupName("微信公众号模块")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zhengqing.wxmp"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    @Bean
    public Docket mallApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                .groupName("商城模块")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zhengqing.mall"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    @Bean
    public Docket testApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                // -Dsa-token.open-url-list
                .groupName("测试API（无需权限，统一放行）")
                .select()
                .paths(PathSelectors.ant("/api/test/**"))
                .build();
        return docket;
    }

    /**
     * swagger-api接口描述信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API文档")
                .description("API文档")
                .termsOfServiceUrl(String.format("127.0.0.1:%s/", this.port))
                .contact(
                        new Contact(
                                "zhengqingya",
                                "https://gitee.com/zhengqingya",
                                "zhengqingya@it.com"
                        )
                )
                .version("1.0.0")
                .build();
    }

    /**
     * 解决Spring Boot 2.6.x以上 与 Swagger 3.0.0 不兼容问题
     * 参考 https://github.com/springfox/springfox/issues/3462
     */
    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier,
                                                                         ServletEndpointsSupplier servletEndpointsSupplier,
                                                                         ControllerEndpointsSupplier controllerEndpointsSupplier,
                                                                         EndpointMediaTypes endpointMediaTypes,
                                                                         CorsEndpointProperties corsProperties,
                                                                         WebEndpointProperties webEndpointProperties,
                                                                         Environment environment) {
        List<ExposableEndpoint<?>> allEndpoints = new ArrayList();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping, null);
    }

    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment, String basePath) {
        return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath) || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
    }

}
