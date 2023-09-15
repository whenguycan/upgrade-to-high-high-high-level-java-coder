package com.ruoyi.project.system.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SysUserVO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 商户全称
     */
    private String nickName;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phonenumber;

    /**
     * 用户性别
     */
    private String sex;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 密码
     */
    private String password;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private Date loginDate;

    /**
     * 会员类型；1-商户；2-会员
     */
    private String memberType;

    /**
     * 微信登录令牌
     */
    private String openId;

    /**
     * 充值账户余额
     */
    private BigDecimal accountValue;

    /**
     * 赠送账户余额
     */
    private BigDecimal giveValue;

    /**
     * 账户总额
     */
    private BigDecimal accountTotal;
}

