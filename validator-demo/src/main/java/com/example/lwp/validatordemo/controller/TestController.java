package com.example.lwp.validatordemo.controller;

import com.example.lwp.validatordemo.vo.Person;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lanwp
 * @Date 2019/9/28 0:31
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public String hello(String name) {
        return "hello " +  name;
    }

    @PostMapping("/test1")
    public String test1(Person person) {
        return ToStringBuilder.reflectionToString(person);
    }

    @PostMapping("/test2")
    public String test2(Person person) {
        return ToStringBuilder.reflectionToString(person);
    }
    @PostMapping("/test3")
    public String test3(Person person, BindingResult bindingResult) {
        print(bindingResult);
        return ToStringBuilder.reflectionToString(person);
    }

    private void print(BindingResult bindingResult){
        Map<String, Object> map = new HashMap<String, Object>();
        if (bindingResult.hasErrors()) {
            List<ObjectError> errorList = bindingResult.getAllErrors();
            List<String> mesList=new ArrayList<String>();
            for (int i = 0; i < errorList.size(); i++) {
                mesList.add(errorList.get(i).getDefaultMessage());
            }
            map.put("status", false);
            map.put("error", mesList);
        } else {
            map.put("status", true);
            map.put("msg", "添加成功");
        }
        System.out.println(ToStringBuilder.reflectionToString(map));;
    }
}
