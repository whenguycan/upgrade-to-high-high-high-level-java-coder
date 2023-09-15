package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

/**
 * 开票头
 */
@Data
public class InvoiceHeadVO {

    /**
     * 会员id
     */
    private Integer userId;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型（1-公司；2-个人）
     */
    private String type;

    /**
     * 税号
     */
    private String taxNum;

    /**
     * 注册地址
     */
    private String registerAddress;

    /**
     * 注册电话
     */
    private String registerPhone;

    /**
     * 开户银行
     */
    private String depositBank;

    /**
     * 开户账号
     */
    private String depositAccount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否是默认 1 默认
     */
    private Integer flagDefault;
}
