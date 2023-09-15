package com.example.demolog.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.*;

/**
 * @Auther: tangwei
 * @Date: 2023/7/13 9:40 AM
 * @Description: 类描述信息
 */
@Import({MyBeanDefinitionRegister.class})
@SpringBootConfiguration
public class MyConfig {


}
