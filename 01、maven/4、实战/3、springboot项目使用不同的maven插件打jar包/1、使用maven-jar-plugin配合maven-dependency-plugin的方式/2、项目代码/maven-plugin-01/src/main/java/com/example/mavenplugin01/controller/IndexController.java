package com.example.mavenplugin01.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: tangwei
 * @Date: 2023/1/1 10:36 PM
 * @Description: 类描述信息
 */
@RestController
public class IndexController {

    @GetMapping("/index")
    public void index(){
        System.out.println("index");
    }
}
