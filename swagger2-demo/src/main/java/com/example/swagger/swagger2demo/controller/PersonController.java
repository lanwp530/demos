package com.example.swagger.swagger2demo.controller;

import com.example.swagger.swagger2demo.vo.Person;
import com.example.swagger.swagger2demo.vo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author lanwp
 * @Date 2019/9/17 7:55
 */
@RestController
@RequestMapping("/person")
@Api(tags = {"person模块"}, description = "PersonController 描述") //
public class PersonController {

    @GetMapping(value = "/person/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    @ApiOperation("person 用户对象接口")
    public Person person(@PathVariable Long id){
        Person person = new Person();
        person.setId(1);
        person.setName("ming字");
        person.setBirthday(new Date());
        return person;
    }
}
