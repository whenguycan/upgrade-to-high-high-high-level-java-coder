package com.ruoyi.framework.web.domain;

import lombok.Data;

/**
 * @author mingchenxu
 * @version 1.0
 * @description: 登录结果，存放token
 * @date 2021/10/27 15:38
 */
@Data
public class LoginResult {

    /**
     * 认证Token
     */
    private String token;

    /**
     * 刷新Token
     */
    private String refreshToken;

    public LoginResult(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
