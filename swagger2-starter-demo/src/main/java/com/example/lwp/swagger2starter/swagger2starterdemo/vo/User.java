package com.example.lwp.swagger2starter.swagger2starterdemo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author lanwp
 * @Date 2019/9/21 8:01
 */
@Data
@ApiModel
public class User {
    private Integer id;
    private String name;
    @Max(200)
    @Min(1)
    private int age;

//    @ApiModelProperty(value = "地址对象")
    @ApiModelProperty("地址对象")
    private Address address1;

    @NotNull
    private String address;
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")
    private String email;

}
