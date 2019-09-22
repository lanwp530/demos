package com.example.lwp.swagger2.swagger2authdemo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lanwp
 * @Date 2019/9/21 8:01
 */
@Data
@ApiModel
public class User {
    private Integer id;
    private String name;

//    @ApiModelProperty(value = "地址对象")
    @ApiModelProperty("地址对象")
    private Address address;
}
