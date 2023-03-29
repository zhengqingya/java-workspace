package com.zhengqing.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p> Knife4j配置类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/6/21 14:01
 */
@Configuration
@EnableSwagger2
public class Knife4jConfig {

    @Value("${server.port}")
    private String port;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.zhengqing.demo"))
                .paths(PathSelectors.any())
                .build();
    }

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
                .version("1.0")
                .build();
    }

}
