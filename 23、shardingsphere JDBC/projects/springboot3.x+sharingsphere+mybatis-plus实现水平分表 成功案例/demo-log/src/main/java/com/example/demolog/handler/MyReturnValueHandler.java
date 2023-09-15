package com.example.demolog.handler;

import com.example.demolog.config.CurrentUser;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @Auther: tangwei
 * @Date: 2023/7/18 2:35 PM
 * @Description: 类描述信息
 */
public class MyReturnValueHandler implements HandlerMethodReturnValueHandler, Ordered {
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        if(returnType.hasMethodAnnotation(CurrentUser.class)){

            System.out.println("MyReturnValueHandler match success");
            return true;
        }
        return false;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        response.addHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().append("123");
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
