package com.example.demolog;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @Auther: tangwei
 * @Date: 2023/7/18 2:18 PM
 * @Description: 类描述信息
 */
@RestControllerAdvice //basePackages如果不指定基础包，默认就是整个项目
public class ExceptionControllerAdvice {
    @ExceptionHandler(Exception.class) //统一异常处理只接收MethodArgumentNotValidException这个异常
    public HashMap returnRes(Exception exc){
        LinkedHashMap<String, String> res = new LinkedHashMap<>();

        System.out.println(exc.getMessage());


        return res;

    }
}
