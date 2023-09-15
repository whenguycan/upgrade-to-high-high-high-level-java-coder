package com.example.agentspringboot;

import com.example.Hello;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Formatter;
import java.util.jar.JarFile;

/**
 * @Auther: tangwei
 * @Date: 2023/5/6 12:33 PM
 * @Description: 类描述信息
 */
@RestController
public class HelloWorldController {

    @GetMapping("/index")
    public void index() throws InterruptedException {

        Module baseModule = Object.class.getModule();
        Module instrumentModule = Instrumentation.class.getModule();

        boolean canRead = baseModule.canRead(instrumentModule);
        String message = String.format("%s can read %s: %s", baseModule.getName(), instrumentModule.getName(), canRead);
        System.out.println(message);



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
