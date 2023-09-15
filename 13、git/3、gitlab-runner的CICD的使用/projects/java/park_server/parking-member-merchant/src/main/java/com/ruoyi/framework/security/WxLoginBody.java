package com.ruoyi.framework.security;

import lombok.Data;

/**
 * wx用户登录对象
 *
 * @author ruoyi
 */
@Data
public class WxLoginBody {

    /**
     * 登录code
     */
    private String loginCode;

}
