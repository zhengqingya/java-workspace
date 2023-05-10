package com.zhengqing.common.swagger.config;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
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
                .extensions(this.openApiExtensionResolver.buildExtensions(this.applicationName));
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

}
