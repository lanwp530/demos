package com.example.lwp.swagger2starter.swagger2starterdemo.controller;

import com.example.lwp.swagger2starter.swagger2starterdemo.vo.User;
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
@RequestMapping("/test1")
//@Api(tags = {"test模块"}, description = "controller描述") //
public class TestController {

//    @ApiOperation(authorizations = )
    @GetMapping(value = "/hello", name = "test1/hello说明")
    public String hello(String username){
        return "hello " + username;
    }

    @GetMapping("/hello1")
    public String hello1(String username){
        return "hello1 " + username;
    }

}
