package com.example.swagger.swagger2demo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Data
@XmlRootElement
public class Person {
    @ApiModelProperty(value = "person主键ID")
    private Integer id;
    @ApiModelProperty(value = "person名字", example = "张三")
    private String name;
    @ApiModelProperty(value = "生日")
    private Date birthday;
}
