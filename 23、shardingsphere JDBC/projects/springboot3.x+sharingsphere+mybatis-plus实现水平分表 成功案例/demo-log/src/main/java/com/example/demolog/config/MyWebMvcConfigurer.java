package com.example.demolog.config;

import com.example.demolog.handler.MyReturnValueHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @Auther: tangwei
 * @Date: 2023/7/17 3:34 PM
 * @Description: 类描述信息
 */
@Component
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MyArgumentResolvers());
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {

        handlers.add(new MyReturnValueHandler());
    }
}
