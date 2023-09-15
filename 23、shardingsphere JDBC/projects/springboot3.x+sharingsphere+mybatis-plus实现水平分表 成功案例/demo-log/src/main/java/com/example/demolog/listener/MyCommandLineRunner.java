package com.example.demolog.listener;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Auther: tangwei
 * @Date: 2023/7/10 1:08 PM
 * @Description: 类描述信息
 */
@Component
public class MyCommandLineRunner  implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("CommandLineRunner run ......");
    }
}
