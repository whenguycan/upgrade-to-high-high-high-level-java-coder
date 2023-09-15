package com.example.agentspringboot;

import com.example.agentspringboot.utils.BitStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.util.Formatter;

/**
 * @Auther: tangwei
 * @Date: 2023/5/6 12:33 PM
 * @Description: 类描述信息
 */
@RestController
public class HelloWorldController {

    @Autowired
    BitStringUtil bitStringUtil;

    @GetMapping("/index")
    public void index() throws InterruptedException {
        System.out.println(bitStringUtil.addString(10));
    }

    @GetMapping("/index2")
    public void index2(){
        System.out.println("index2");
    }

    private static void printURLS(String title, URL[] urls) {
        StringBuilder sb = new StringBuilder();
        Formatter fm = new Formatter(sb);
        fm.format("=========%s=========%n", title);
        for (URL url : urls) {
            fm.format("--->%s%n", url.toExternalForm());
        }
        System.out.println(sb);
    }
}
