package com.example.swagger.swagger2demo.controller;

import com.example.swagger.swagger2demo.vo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
