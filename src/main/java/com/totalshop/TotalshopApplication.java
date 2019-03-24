package com.totalshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com"})
@MapperScan("com.dao")
public class TotalshopApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(TotalshopApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
        //这里指向原先用main方法执行的Application启动类
        return builder.sources(TotalshopApplication.class);
    }
}

