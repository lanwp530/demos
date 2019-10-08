package com.lwp.spring.springexceptionhandle.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lanwp
 * @Date 2019/9/26 22:24
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @GetMapping("/test1")
    @ResponseStatus(HttpStatus.OK)
    public String test1(){
        throw new RuntimeException("test1 exception");
//        return "test1";
    }

}
