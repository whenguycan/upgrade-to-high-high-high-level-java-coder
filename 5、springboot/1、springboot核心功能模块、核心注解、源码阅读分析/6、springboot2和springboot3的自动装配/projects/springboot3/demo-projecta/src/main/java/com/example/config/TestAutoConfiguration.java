package com.example.config;

import com.example.utils.DemoUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: tangwei
 * @Date: 2023/6/30 9:53 AM
 * @Description: 类描述信息
 */
@Configuration
public class TestAutoConfiguration {

    @Bean
    public DemoUtil demoUtil(){

        System.out.println("new demo");
        return new DemoUtil();
    }
}
