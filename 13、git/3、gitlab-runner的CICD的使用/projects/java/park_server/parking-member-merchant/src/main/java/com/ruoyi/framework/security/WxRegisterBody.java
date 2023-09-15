package com.ruoyi.framework.security;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * wx用户注册对象
 *
 * @author ruoyi
 */
@Data
public class WxRegisterBody {

    /**
     * openId
     */
//    @NotBlank(message = "openId不能为空")
    private String openId;

    /**
     * 手机号码
     */
    @NotBlank(message = "手机号码不能为空")
    private String phonenumber;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String smsCode;

    /** 会员类型；1-商户；2-会员 */
    @NotBlank(message = "会员类型不能为空")
    private String memberType;

}
