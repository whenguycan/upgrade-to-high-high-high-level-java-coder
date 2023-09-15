package com.ruoyi.framework.security.relogin;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.framework.security.LoginBody;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 切换用户登陆鉴权 Provider，要求实现 AuthenticationProvider 接口
 *
 * @author zh
 */
public class ReLoginAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ReLoginAuthenticationToken authenticationToken = (ReLoginAuthenticationToken) authentication;

        LoginBody loginBody = (LoginBody) authenticationToken.getPrincipal();

        UserDetails userDetails = userDetailsService.loadUserByUsername(JSON.toJSONString(loginBody));
        // 此时鉴权成功后，应当重新 new 一个拥有鉴权的 authenticationResult 返回
        ReLoginAuthenticationToken authenticationResult = new ReLoginAuthenticationToken(userDetails, userDetails.getAuthorities());

        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        // 判断 authentication 是不是 SmsCodeAuthenticationToken 的子类或子接口
        return ReLoginAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
