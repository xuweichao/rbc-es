package com.elens.data.rbces.conf;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @BelongsProject: elensdata-oauth
 * @BelongsPackage: com.elens.data.oauth.manager.conf
 * @Author: xuweichao
 * @CreateTime: 2018-11-08 19:13
 * @Description: swagger 配置
 * http:localhost:port/swagger-ui.html
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket apiDoc() {
        return docket();
    }

    private Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).select()
//                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))//这里采用包含注解的方式来确定要显示的接口
//                .apis(RequestHandlerSelectors.basePackage("com.elens.data.oauth.manager.controller"))    //这里采用包扫描的方式来确定要显示的接口
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .ignoredParameterTypes(Errors.class)
                .apiInfo(apiInfo());

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Swagger2 api文档")
                .description("Swagger2 api文档")
                .version("1.0.0")
                .build();

    }
}

