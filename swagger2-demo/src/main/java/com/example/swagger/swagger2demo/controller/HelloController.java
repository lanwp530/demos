package com.example.swagger.swagger2demo.controller;

import com.example.swagger.swagger2demo.vo.User;
import com.google.common.base.MoreObjects;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.util.ToStringUtil;
import org.springframework.core.style.DefaultToStringStyler;
import org.springframework.core.style.ToStringCreator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lanwp
 * @Date 2019/9/17 7:55
 */
@RestController
@RequestMapping("/test")
//@Api // tags默认值 hello-controller description默认值Hello Controller
//@Api(value = "/test", description = "controller描述")
@Api(tags = {"test模块"}, description = "controller描述") //
//@Api(value = "/test", tags = {"抽奖模块"}, description = "用户信息管理控制层")
//@Api(value = "/test", tags = {"抽奖模块"}, description = "随机的控制层")
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
