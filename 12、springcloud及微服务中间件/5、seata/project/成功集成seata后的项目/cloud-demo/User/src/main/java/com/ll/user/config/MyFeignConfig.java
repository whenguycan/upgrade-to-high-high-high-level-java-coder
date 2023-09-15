package com.ll.user.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: tangwei
 * @Date: 2023/4/23 10:33 AM
 * @Description: 类描述信息
 */
@Configuration
public class MyFeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor(){
        RequestInterceptor requestInterceptorTmp = new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                requestTemplate.header("username", "tangwei");
            }
        };

        return requestInterceptorTmp;
    }
}
