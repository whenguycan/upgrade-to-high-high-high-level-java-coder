package com.example.demolog;

import com.example.demolog.config.Cat;
import com.example.demolog.config.Duck;
import com.example.demolog.config.User;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@MapperScan(basePackages = {"com.example.demolog.dao"})
@SpringBootApplication
@ComponentScan("com.example")
public class DemoLogApplication {

    public static void main(String[] args) {
        var ioc = SpringApplication.run(DemoLogApplication.class, args);

        for(String beanName: ioc.getBeanNamesForType(Duck.class)){
            System.out.println("************************");
            System.out.println(beanName);
        }


    }

}
