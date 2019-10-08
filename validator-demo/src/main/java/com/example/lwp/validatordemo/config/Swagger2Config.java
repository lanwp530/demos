package com.example.lwp.validatordemo.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
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
@EnableSwaggerBootstrapUI
@Import(springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class)
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
//                .enable(false) // 是否启用swagger
                .apiInfo(apiInfo())
//                .pathMapping("/")
                .select()
                // 指定当前包路径，这里就添加了两个包，注意方法变成了basePackage，中间加上成员变量splitor
                // .apis(basePackage("com.XX.api.controller.broad;com.XX.api.controller.village"))
                .apis(RequestHandlerSelectors.basePackage("com.example.lwp.validatordemo.controller")) // 不填写路径会显示所有controller
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("springboot利用swagger构建api文档")
                .description("简单优雅的restful风格")
                .version("1.0.0")
                .contact(new Contact("张三", "blog.csdn.net", "zhangsan@gmail.com"))
//                .contact(new Contact("李四", "blog.csdn.net", "zhangsan@gmail.com"))
                .termsOfServiceUrl("http://xxx:8080/") // 服务器url
//                        .license("The Apache License")
//                        .licenseUrl("http://www.baidu.com")
                .build();
    }
}
