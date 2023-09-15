package com.ruoyi.framework.interceptor;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpHelper;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysUserService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Component
public class AccountAccessInterceptor implements HandlerInterceptor {


    @Resource
    ISysUserService userService;

    /**
     * 预处理回调方法，实现处理器的预处理（如检查登陆），第三个参数为响应的处理器，自定义Controller
     * 返回值：true表示继续流程（如调用下一个拦截器或处理器）；false表示流程中断（如登录检查失败），不会继续调用其他的拦截器或处理器，此时我们需要通过response来产生响应；
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String nowParams = "";
        nowParams = HttpHelper.getBodyString(request);
        // body参数为空，获取Parameter的数据
        if (StringUtils.isEmpty(nowParams)) {
            nowParams = JSON.toJSONString(request.getParameterMap());
        }
        SysUser sysUser = userService.selectUserById(SecurityUtils.getUserId());
        if ("1".equals(sysUser.getFreezeFlag())) {
            return false;
        }
        return true;
    }

}
