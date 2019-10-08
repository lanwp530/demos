package com.example.lwp.shiro.shirodemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lanwp
 * @Date 2019/9/22 17:23
 */
@RestController
@RequestMapping("/hello")
public class HelloContoller {

    @GetMapping("/hello")
    public String hello(String name){
        return "hello" + name;
    }
}
