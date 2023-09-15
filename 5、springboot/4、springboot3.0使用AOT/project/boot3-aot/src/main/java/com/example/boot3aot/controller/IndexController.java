package com.example.boot3aot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: tangwei
 * @Date: 2023/6/26 4:54 PM
 * @Description: 类描述信息
 */
@RestController
public class IndexController {

    @GetMapping("/index")
    public void index(){
        System.out.println("first native springboot3");
    }
}
