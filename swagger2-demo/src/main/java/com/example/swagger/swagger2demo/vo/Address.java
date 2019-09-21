package com.example.swagger.swagger2demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lanwp
 * @Date 2019/9/21 8:04
 */
@Data
@ApiModel
public class Address {
    /**
     * name默认字段名
     */
    @ApiModelProperty(value = "街道",required = true, example = "测试街道")
    private String street;
    @ApiModelProperty("省份")
    private String province;
//    @ApiModelProperty(value = "城市",allowableValues= "1,2,3")
    @ApiModelProperty(allowableValues= "1;2;3")
    private String city;
}
