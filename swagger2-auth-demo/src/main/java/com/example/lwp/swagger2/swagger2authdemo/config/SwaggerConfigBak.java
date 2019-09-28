//package com.example.lwp.swagger2.swagger2authdemo.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.*;
//import springfox.documentation.service.*;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spi.service.contexts.SecurityContext;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger.web.SecurityConfiguration;
//import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import static com.google.common.collect.Lists.newArrayList;
//
///**
// * @author lanwp
// * @Date 2019/9/17 7:53
// */
//@Configuration
//@EnableSwagger2
////@EnableSwaggerBootstrapUI
//public class SwaggerConfigBak {
//
//    @Bean
//    public Docket createRestApi() {
//        Docket docket = new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
////                .pathMapping("/")
//                .select()
//                // 指定当前包路径，这里就添加了两个包，注意方法变成了basePackage，中间加上成员变量splitor
//                // .apis(basePackage("com.XX.api.controller.broad;com.XX.api.controller.village"))
//                .apis(RequestHandlerSelectors.basePackage("com.example.lwp.swagger2.swagger2authdemo.controller"))
//                .paths(PathSelectors.any())
//                .build()
//                // 增加授权
//                .securitySchemes(securitySchemes())
//                .securityContexts(securityContexts())
//                ;
//        return docket;
//    }
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("SpringBoot整合Swagger")
//                .description("SpringBoot整合Swagger，详细信息......")
//                .version("9.0")
//                .contact(new Contact("张三", "blog.csdn.net", "zhangsan@gmail.com"))
//                .contact(new Contact("李四", "blog.csdn.net", "zhangsan@gmail.com"))
////                        .license("The Apache License")
////                        .licenseUrl("http://www.baidu.com")
//                .build();
//    }
//
//    private List<SecurityScheme> securitySchemes(){
//        List<SecurityScheme> list = new ArrayList<>();
//        list.add(new ApiKey("access_token", "access_token鉴权值", "header"));
////        list.add(new ApiKey("access_token1", "access_token1鉴权值", "query"));
//        return list;
//    }
//
//    private AuthorizationScope[] scopes() {
//        AuthorizationScope[] scopes = {
//                new AuthorizationScope("read", "for read operations"),
//                new AuthorizationScope("write", "for write operations"),
//                new AuthorizationScope("foo", "Access foo API") };
//        return scopes;
//    }
//
//    private SecurityScheme securityScheme() {
//        GrantType grantType = new AuthorizationCodeGrantBuilder()
//                .tokenEndpoint(new TokenEndpoint(AUTH_SERVER + "/token", "oauthtoken"))
//                .tokenRequestEndpoint(
//                        new TokenRequestEndpoint(AUTH_SERVER + "/authorize", CLIENT_ID, CLIENT_ID))
//                .build();
//
//        SecurityScheme oauth = new OAuthBuilder().name("spring_oauth")
//                .grantTypes(Arrays.asList(grantType))
//                .scopes(Arrays.asList(scopes()))
//                .build();
//        return oauth;
//    }
//
//    private List<SecurityContext> securityContexts() {
//        return newArrayList(
//                SecurityContext.builder()
//                        .securityReferences(defaultAuth())
//                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
//                        .build()
//        );
//    }
//
//    List<SecurityReference> defaultAuth() {
//        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        return newArrayList(
//                new SecurityReference("Authorization", authorizationScopes));
//    }
//
//    @Bean
//    public SecurityConfiguration security() {
//        String CLIENT_ID = "CLIENT_ID";
//        String CLIENT_SECRET = "CLIENT_SECRET";
//        return SecurityConfigurationBuilder.builder()
//                .clientId(CLIENT_ID)
//                .clientSecret(CLIENT_SECRET)
//                .scopeSeparator(" ")
//                .useBasicAuthenticationWithAccessCodeGrant(true)
//                .build();
//    }
//}
