package com.lwp.spring.springexceptionhandle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lanwp
 * @Date 2019/9/26 22:45
 */
@RequestMapping("/test")
@Controller
public class TestController {

    @GetMapping("/t1")
    public ModelAndView t1(){
        Map map = new HashMap<>();
        map.put("name", "名字");
        map.put("id", 123);
        return new ModelAndView(new MappingJackson2JsonView(), map);
    }
    @GetMapping("/t2")
    public ModelAndView t2(){
        return new ModelAndView("error.jsp");
    }
}
