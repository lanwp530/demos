package com.example.lwp.validatordemo.controller;

import com.example.lwp.validatordemo.vo.Person;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lanwp
 * @Date 2019/9/28 0:31
 */
@RestController
@RequestMapping("/person")
public class PersonController {

    @GetMapping("/hello")
    public String hello(String name) {
        return "hello " +  name;
    }

    @PostMapping("/save")
    public String save(@Valid Person person) {
        return ToStringBuilder.reflectionToString(person);
    }

    @PostMapping("/save1")
    public Person save1(@Valid Person person) {
        return person;
    }

    @PostMapping("/save2")
    public Person save2(@Validated Person person) {

        return person;
    }

    /**
     * 有 BindingResult bindingResult 值 ，统一异常信息拦截不了
     * @param person
     * @param bindingResult
     * @return
     */
    @PostMapping("/save3")
    public List<ObjectError> save3(@Valid Person person, BindingResult bindingResult) {
        return bindingResult.getAllErrors();
    }

    /**
     * 有 BindingResult bindingResult 值 ，统一异常信息拦截不了
     * @param person
     * @param bindingResult
     * @return
     */
    @PostMapping("/save4")
    public List<ObjectError> save4(@Validated Person person, BindingResult bindingResult) {

        return bindingResult.getAllErrors();
    }
}
