package com.example.demolog.listener;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @Auther: tangwei
 * @Date: 2023/7/7 2:39 PM
 * @Description: 类描述信息
 */
public class MyApplicationContextInitializer implements ApplicationContextInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        System.out.println("xxxxx......");
    }
}
