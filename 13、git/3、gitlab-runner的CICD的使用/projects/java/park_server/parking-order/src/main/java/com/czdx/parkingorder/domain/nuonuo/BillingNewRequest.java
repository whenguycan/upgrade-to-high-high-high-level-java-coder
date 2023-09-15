package com.czdx.parkingorder.domain.nuonuo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 诺诺发票开放平台 请求开具发票接口2.0请求参数
 * </p>
 *
 * @author 琴声何来
 * @since 2023/4/7 9:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingNewRequest {
    /**
     * 购方名称
     */
    private String buyerName;

    /**
     * 购方税号（企业要填，个人可为空）
     */
    private String buyerTaxNum;

    /**
     * 购方电话（发票上显示）
     */
    private String buyerTel;

    /**
     * 购方地址（发票上显示）
     */
    private String buyerAddress;

    /**
     * 购方银行账号及开户行地址（发票上显示）。建议以“地址+账号”格式
     */
    private String buyerAccount;

    /**
     * 推送方式：-1,不推送;0,邮箱;1,手机（默认）;2,邮箱、手机
     */
    private String pushMode;

    /**
     * 购方手机（推送用）
     */
    private String buyerPhone;

    /**
     * 购方邮箱（推送用）
     */
    private String email;

    /**
     * 销方税号
     */
    private String salerTaxNum;

    /**
     * 销方电话
     */
    private String salerTel;

    /**
     * 销方地址
     */
    private String salerAddress;

    /**
     * 订单号（每个企业唯一）
     */
    private String orderNo;

    /**
     * 订单时间
     */
    private LocalDateTime invoiceDate;

    /**
     * 收款人
     */
    private String payee;

    /**
     * 复核人
     */
    private String checker;

    /**
     * 开票员
     */
    private String clerk;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 开票类型（1-蓝票；2-红票）
     */
    private String invoiceType;

    /**
     * 开票完成回传发票信息地址
     */
    private String callBackUrl;

    /**
     * 发票明细，支持填写商品明细最大2000行（包含折扣行、被折扣行）
     */
    private List<InvoiceDetail> invoiceDetail;
}
