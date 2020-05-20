package com.example.httpclientdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author lanwenping
 * @version 1.0
 * @date 2020/3/19 14:09
 */

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/test")
    public void test(String name) {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://www.baidu.com", String.class);
        System.out.println(forEntity.getStatusCode());
        System.out.println(forEntity.getBody());
    }
}
