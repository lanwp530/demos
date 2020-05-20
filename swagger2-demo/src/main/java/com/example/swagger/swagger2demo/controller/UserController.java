package com.example.swagger.swagger2demo.controller;

import com.example.swagger.swagger2demo.vo.Address;
import com.example.swagger.swagger2demo.vo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.bind.annotation.*;

/**
 * @author lanwp
 * @Date 2019/9/17 7:55
 */
@RestController
@RequestMapping("/user")
@Api(tags = {"user模块"}, description = "UserController 描述") //
public class UserController {

    @PostMapping("/helloUser")
    @ApiOperation("hello user 用户对象接口")
    public User helloUser(User user){
        System.out.println("hello " + user.toString());
        return user;
    }

    @GetMapping(value = "/{id}",produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation("getUser接口")
    public User getUser(@PathVariable Long id){
//        ContentNegotiationManagerFactoryBean
        User user = new User();
        user.setId(1);
        user.setName("name");
        user.setAddress(new Address());
        return user;
    }
}
