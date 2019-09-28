package com.example.lwp.swagger2.swagger2authdemo.controller;

import com.example.lwp.swagger2.swagger2authdemo.vo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lanwp
 * @Date 2019/9/17 7:55
 */
@RestController
@RequestMapping("/auth")
@Api(tags = {"auth模块"}, description = "需要鉴权的模块(描述)") //
public class AuthController {

    @GetMapping("/hello")
//    @ApiOperation(value = "hello入口", authorizations = {@Authorization(value="access_token")})
    @ApiOperation(value = "hello入口")
    public String hello(String username){
        return "hello " + username;
    }

    @GetMapping("/hello1")
//    @ApiOperation(value = "auth hello1接口", authorizations = {@Authorization(value="basicAuth")})
    @ApiOperation(value = "auth hello1接口")
    public String hello1(String username){
        return "hello1 " + username;
    }

//    @PostMapping("/helloUser")
    @PostMapping(value = "/helloUser", name = "Hello用户")
//    @ApiOperation("hello user 用户对象接口")
    @ApiOperation(value = "接口标题：hello user 用户对象接口", tags = {"tag1", "tag2"}, notes = "notes content 接口描述内容")
    public User helloUser(User user){
        System.out.println("hello " + user.toString());
        return user;
    }
}
