package com.example.swagger.swagger2demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lanwp
 * @Date 2019/9/17 7:55
 */
@RestController
@RequestMapping("/test")
@Api("用户信息管理")
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
}
