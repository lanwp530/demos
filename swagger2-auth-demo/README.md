# swagger2使用

spring-boot使用swagger2

> Api接口文档生成工具 https://baijiahao.baidu.com/s?id=1634315317161175683&wfr=spider&for=pc

## 1.引入maven依赖

```s
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger2</artifactId>
        <version>2.9.2</version>
    </dependency>
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger-ui</artifactId>
        <version>2.9.2</version>
    </dependency>
```

## 2.配置类Swagger2Config

```
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

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
//                .pathMapping("/")
//                .pathMapping(pathMapping)
                .select()    .apis(RequestHandlerSelectors.basePackage("com.example.swagger.swagger2demo.controller"))
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
//                        .license("The Apache License")
//                        .licenseUrl("http://www.baidu.com")
                .build();
    }
}
```

## 3.controller注解

```
@RestController
@RequestMapping("/test")
@Api(tags = {"test模块"}, description = "controller描述")
//@Api(value = "/test", tags = {"抽奖模块"}, description = "用户信息管理控制层")
public class HelloController {

    @GetMapping("/hello")
    @ApiOperation("hello入口")
    public String hello(String username){
        return "hello " + username;
    }

    @GetMapping("/hello1")
    public String hello1(String username){
        return "hello1 " + username;
    }

    @PostMapping("/helloUser")
    @ApiOperation("hello user 用户对象接口")
    public User helloUser(User user){
        System.out.println("hello " + user.toString());
        return user;
    }
}
```

## 4.对象地址

User

```
@Data
@ApiModel
public class User {
    private Integer id;
    private String name;

//    @ApiModelProperty(value = "地址对象")
    @ApiModelProperty("地址对象")
    private Address address;
}
```

Address

```
@Data
@ApiModel
public class Address {
    /**
     * name默认字段名
     */
    @ApiModelProperty(value = "街道",required = true, example = "测试街道")
    private String street;
    @ApiModelProperty("省份")
    private String province;
//    @ApiModelProperty(value = "城市",allowableValues= "1,2,3")
    @ApiModelProperty(allowableValues= "1;2;3")
    private String city;
}
```

## 5.访问地址

访问地址/swagger-ui.html

http://localhost:8080/swagger-ui.html



# swagger2使用swagger-bootstrap-ui

## 1.引入maven依赖

```
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.9.2</version>
</dependency>

<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>swagger-bootstrap-ui</artifactId>
    <version>1.9.6</version>
</dependency>
```

## 2.启用

​	使用@EnableSwagger2即可，如果需要打开SwaggerBootstrapUI增强功能启用 `@EnableSwaggerBootstrapUI` 注解即可。

```
@Configuration
@EnableSwagger2
//@EnableSwaggerBootstrapUI
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
//                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.swagger.swagger2demo.controller"))
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
//                        .license("The Apache License")
//                        .licenseUrl("http://www.baidu.com")
                .build();
    }
}
```

## 3.访问地址

doc.html

http://localhost:8080/doc.html



# swagger2增加授权

简单说就是增加参数校验，使用swagger2请求时，会带这些参数

```
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
```

