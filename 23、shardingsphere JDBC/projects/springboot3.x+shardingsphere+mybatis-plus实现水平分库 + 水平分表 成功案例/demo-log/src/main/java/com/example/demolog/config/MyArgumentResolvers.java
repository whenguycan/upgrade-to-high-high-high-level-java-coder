package com.example.demolog.config;

import com.example.demolog.vo.UserVo;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @Auther: tangwei
 * @Date: 2023/7/17 3:35 PM
 * @Description: 类描述信息
 */

public class MyArgumentResolvers implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        if(methodParameter.hasParameterAnnotation(CurrentUser.class)){

            System.out.println("===========================》符合条件");
            return true;
        }
        return false;
    }
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        System.out.println("------------------------------------------");
        UserVo target=new UserVo();
        target.setAge(91);
        target.setName("tangwei");
        target.setScore(100.00F);
        return target;
    }
}
