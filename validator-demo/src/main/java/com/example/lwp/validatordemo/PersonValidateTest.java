package com.example.lwp.validatordemo;

import com.example.lwp.validatordemo.vo.Person;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;

/**
 * @author lanwp
 * @Date 2019/9/28 0:13
 */
public class PersonValidateTest {
    public static void main(String[] args) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        /* 信息封装 */
        Person infoBean = new Person();
        infoBean.setId(-1);
        infoBean.setUserName("张三");
        infoBean.setPassWord("123456");
        infoBean.setEmail("test@test.com");
        infoBean.setBirthday(getDate(LocalDateTime.of(2019, Month.of(10), 1, 0, 0)));
        infoBean.setBirthday(new Date());

        /* 将类型装载效验 */
        Set<ConstraintViolation<Person>> set = validator.validate(infoBean);
        for (ConstraintViolation<Person> constraintViolation : set) {
            System.out.println("错误：" + constraintViolation.getMessage());
        }
    }

    public static Date getDate(LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);

        Date date = Date.from(zdt.toInstant());

        System.out.println("LocalDateTime = " + localDateTime);
        System.out.println("Date = " + date);

        return date;
    }
}
