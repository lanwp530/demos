package com.lwp.example.springbootmybatis.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.lwp.example.springbootmybatis.core.base.BaseController;
import com.lwp.example.springbootmybatis.mapper.AuthorMapper;
import com.lwp.example.springbootmybatis.model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController
 *
 * @author lanwenping
 * @version 1.0
 * @date 2020/5/22 12:43
 */
@RestController
public class TestController extends BaseController {

    @Autowired
    AuthorMapper authorMapper;

    @RequestMapping("/test")
    public String test(String name) {
        String content = "hello " + name;
        System.out.println(content);
        System.out.println(content);
        return content;
    }

    @RequestMapping("/mybatis")
    public String test1(String name) {
        String content = "hello " + name;
        Author author = new Author();
//        author.setId(1L);
        author.setUsername("1");
        author.setPassword("1");
        author.setEmail("11");
        author.setBio("11");
        author.setFavouriteSection("111");
        System.out.println(1111);
        authorMapper.insert(author);
        System.out.println(content);
        System.out.println(content);
//        authorMapper.updateByExample()
        return content;
    }

    @RequestMapping("/get")
    public String get(String name) {
        String content = JSON.toJSONString(authorMapper.selectByPrimaryKey(1L));
        return content;
    }
}
