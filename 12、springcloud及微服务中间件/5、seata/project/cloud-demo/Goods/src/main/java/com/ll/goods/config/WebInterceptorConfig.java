package com.ll.goods.config;

import com.ll.goods.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Auther: tangwei
 * @Date: 2023/4/23 10:41 AM
 * @Description: 类描述信息
 */
@Configuration
public class WebInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    public LoginInterceptor loginInterceptor; //上面的拦截器加载进来

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**");//.excludePathPatterns(new String[]{"/login", "/logout"});
    }
}
