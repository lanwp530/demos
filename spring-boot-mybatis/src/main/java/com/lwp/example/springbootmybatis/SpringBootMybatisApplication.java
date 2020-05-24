package com.lwp.example.springbootmybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
//@MapperScan("com.demo.*.mapper")
//@MapperScan("com.demo.**.mapper")
@MapperScans(
        @MapperScan(basePackages = "com.lwp.example.springbootmybatis.**.mapper")
)
public class SpringBootMybatisApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMybatisApplication.class, args);
    }

    @Autowired
    Environment environment;


    @Override
    public void run(String... args) throws Exception {
//        spring.profiles.active
        // 当前激活的环境，多个值
        System.out.println(environment.getActiveProfiles());
        // 默认 default
        System.out.println(environment.getDefaultProfiles());
        // 取得当前spring.profiles.active
        System.out.println(environment.getProperty("spring.profiles.active"));
    }
}
