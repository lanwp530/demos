package com.example.lwp.validatordemo.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * @author lanwp
 * @Date 2019/9/28 0:05
 */
@ApiModel
@Data
public class Person {
    @NotNull(message = "姓名不能为空!")
    @Min(value = 1, message = "Id只能大于等于1，小于等于10")
    @Max(value = 10, message = "Id只能大于等于1，小于等于10")
    private Integer id;

    @NotNull(message = "姓名不能为空!")
    @Size(min = 2, max = 4, message = "姓名长度必须在{min}和{max}之间")
    @Pattern(regexp = "[\u4e00-\u9fa5]+", message = "名称只能输入是中文字符")
    private String userName;

    @NotNull(message = "密码不能为空!")
    @Size(min = 6, max = 12, message = "密码长度必须在{min}和{max}之间")
    private String passWord;

    @NotNull(message = "日期不能为空!")
    @Past(message = "你只能输入过去的日期")
    private Date birthday;

    @NotNull(message = "邮件不能为空!")
    @Email(message = "邮件格式不正确")
    private String email;
}
