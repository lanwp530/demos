package com.example.lwp.swagger2.swagger2authdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lanwp
 * @Date 2019/9/17 7:53
 */
@Configuration
@EnableSwagger2
//@EnableSwaggerBootstrapUI
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securitySchemes(createSecuritySchemeList())
//                .pathMapping("/")
                .select()
                // 指定当前包路径，这里就添加了两个包，注意方法变成了basePackage，中间加上成员变量splitor
                // .apis(basePackage("com.XX.api.controller.broad;com.XX.api.controller.village"))
                .apis(RequestHandlerSelectors.basePackage("com.example.lwp.swagger2.swagger2authdemo.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SpringBoot整合Swagger")
                .description("SpringBoot整合Swagger，详细信息......")
                .version("9.0")
                .contact(new Contact("张三", "blog.csdn.net", "zhangsan@gmail.com"))
                .contact(new Contact("李四", "blog.csdn.net", "zhangsan@gmail.com"))
//                        .license("The Apache License")
//                        .licenseUrl("http://www.baidu.com")
                .build();
    }

    private List<SecurityScheme> createSecuritySchemeList(){
        List<SecurityScheme> list = new ArrayList<>();
        list.add(new ApiKey("access_token", "access_token鉴权值", "header"));
        list.add(new ApiKey("access_token1", "access_token1鉴权值", "query"));
        return list;
    }
}
