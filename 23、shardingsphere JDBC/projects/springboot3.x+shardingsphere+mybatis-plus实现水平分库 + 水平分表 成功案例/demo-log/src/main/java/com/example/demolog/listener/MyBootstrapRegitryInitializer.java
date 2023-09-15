package com.example.demolog.listener;

import org.springframework.boot.BootstrapRegistry;
import org.springframework.boot.BootstrapRegistryInitializer;

/**
 * @Auther: tangwei
 * @Date: 2023/7/7 1:29 PM
 * @Description: 类描述信息
 */
public class MyBootstrapRegitryInitializer implements BootstrapRegistryInitializer {
    @Override
    public void initialize(BootstrapRegistry registry) {
        System.out.println("init........");
    }
}
