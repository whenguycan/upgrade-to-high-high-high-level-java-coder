package com.czdx.parkingpayment.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 支付宝回调通知记录表
 * @TableName t_alipay_notification
 */
@TableName(value ="t_alipay_notification")
@Data
public class AlipayNotification implements Serializable {
    /**
     * 逻辑ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 原支付请求的商户订单号。
     */
    @TableField(value = "out_trade_no")
    private String outTradeNo;

    /**
     * 支付宝交易凭证号。
     */
    @TableField(value = "trade_no")
    private String tradeNo;

    /**
     * 商户业务 ID，主要是退款通知中返回退款申请的流水号。
     */
    @TableField(value = "out_biz_no")
    private String outBizNo;

    /**
     * 通知的发送时间。
     */
    @TableField(value = "notify_time")
    private LocalDateTime notifyTime;

    /**
     * 通知的类型。
     */
    @TableField(value = "notify_type")
    private String notifyType;

    /**
     * 通知校验 ID。
     */
    @TableField(value = "notify_id")
    private String notifyId;

    /**
     * 支付宝分配给开发者的应用 ID。
     */
    @TableField(value = "app_id")
    private String appId;

    /**
     * 编码格式
     */
    @TableField(value = "charset")
    private String charset;

    /**
     * 调用的接口版本，固定为：1.0。
     */
    @TableField(value = "version")
    private String version;

    /**
     * 商家生成签名字符串所使用的签名算法类型，目前支持 RSA2 和 RSA，推荐使用 RSA2。
     */
    @TableField(value = "sign_type")
    private String signType;

    /**
     * 签名。
     */
    @TableField(value = "sign")
    private String sign;

    /**
     * 买家支付宝账号对应的支付宝唯一用户号。
     */
    @TableField(value = "buyer_id")
    private String buyerId;

    /**
     * 买家支付宝账号。
     */
    @TableField(value = "buyer_logon_id")
    private String buyerLogonId;

    /**
     * 卖家支付宝用户号。
     */
    @TableField(value = "seller_id")
    private String sellerId;

    /**
     * 卖家支付宝账号。
     */
    @TableField(value = "seller_email")
    private String sellerEmail;

    /**
     * 交易目前所处的状态。
     */
    @TableField(value = "trade_status")
    private String tradeStatus;

    /**
     * 本次交易支付的订单金额，单位为人民币（元）。
     */
    @TableField(value = "total_amount")
    private BigDecimal totalAmount;

    /**
     * 商家在收益中实际收到的款项，单位人民币（元）。
     */
    @TableField(value = "receipt_amount")
    private BigDecimal receiptAmount;

    /**
     * 用户在交易中支付的可开发票的金额。
     */
    @TableField(value = "invoice_amount")
    private BigDecimal invoiceAmount;

    /**
     * 用户在交易中支付的金额。
     */
    @TableField(value = "buyer_pay_amount")
    private BigDecimal buyerPayAmount;

    /**
     * 使用集分宝支付的金额。
     */
    @TableField(value = "point_amount")
    private BigDecimal pointAmount;

    /**
     * 退款通知中，返回总退款金额，单位为人民币（元），支持两位小数。
     */
    @TableField(value = "refund_fee")
    private BigDecimal refundFee;

    /**
     * 商品的标题/交易标题/订单标题/订单关键字等，是请求时对应的参数，原样通知回来。
     */
    @TableField(value = "subject")
    private String subject;

    /**
     * 订单的备注、描述、明细等。对应请求时的 body 参数，原样通知回来。
     */
    @TableField(value = "body")
    private String body;

    /**
     * 该笔交易创建的时间。
     */
    @TableField(value = "gmt_create")
    private LocalDateTime gmtCreate;

    /**
     * 该笔交易 的买家付款时间。
     */
    @TableField(value = "gmt_payment")
    private LocalDateTime gmtPayment;

    /**
     * 该笔交易的退款时间。
     */
    @TableField(value = "gmt_refund")
    private LocalDateTime gmtRefund;

    /**
     * 该笔交易结束时间。
     */
    @TableField(value = "gmt_close")
    private LocalDateTime gmtClose;

    /**
     * 支付成功的各个渠道金额信息。
     */
    @TableField(value = "fund_bill_list")
    private String fundBillList;

    /**
     * 公共回传参数，如果请求时传递了该参数，则返回给商家时会在异步通知时将该参数原样返回。本参数必须进行 UrlEncode 之后才可以发送给支付宝。
     */
    @TableField(value = "passback_params")
    private String passbackParams;

    /**
     * 本交易支付时所有优惠券信息。
     */
    @TableField(value = "voucher_detail_list")
    private String voucherDetailList;

    /**
     * 
     */
    @TableField(value = "auth_app_id")
    private String authAppId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        AlipayNotification other = (AlipayNotification) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOutTradeNo() == null ? other.getOutTradeNo() == null : this.getOutTradeNo().equals(other.getOutTradeNo()))
            && (this.getTradeNo() == null ? other.getTradeNo() == null : this.getTradeNo().equals(other.getTradeNo()))
            && (this.getOutBizNo() == null ? other.getOutBizNo() == null : this.getOutBizNo().equals(other.getOutBizNo()))
            && (this.getNotifyTime() == null ? other.getNotifyTime() == null : this.getNotifyTime().equals(other.getNotifyTime()))
            && (this.getNotifyType() == null ? other.getNotifyType() == null : this.getNotifyType().equals(other.getNotifyType()))
            && (this.getNotifyId() == null ? other.getNotifyId() == null : this.getNotifyId().equals(other.getNotifyId()))
            && (this.getAppId() == null ? other.getAppId() == null : this.getAppId().equals(other.getAppId()))
            && (this.getCharset() == null ? other.getCharset() == null : this.getCharset().equals(other.getCharset()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
            && (this.getSignType() == null ? other.getSignType() == null : this.getSignType().equals(other.getSignType()))
            && (this.getSign() == null ? other.getSign() == null : this.getSign().equals(other.getSign()))
            && (this.getBuyerId() == null ? other.getBuyerId() == null : this.getBuyerId().equals(other.getBuyerId()))
            && (this.getBuyerLogonId() == null ? other.getBuyerLogonId() == null : this.getBuyerLogonId().equals(other.getBuyerLogonId()))
            && (this.getSellerId() == null ? other.getSellerId() == null : this.getSellerId().equals(other.getSellerId()))
            && (this.getSellerEmail() == null ? other.getSellerEmail() == null : this.getSellerEmail().equals(other.getSellerEmail()))
            && (this.getTradeStatus() == null ? other.getTradeStatus() == null : this.getTradeStatus().equals(other.getTradeStatus()))
            && (this.getTotalAmount() == null ? other.getTotalAmount() == null : this.getTotalAmount().equals(other.getTotalAmount()))
            && (this.getReceiptAmount() == null ? other.getReceiptAmount() == null : this.getReceiptAmount().equals(other.getReceiptAmount()))
            && (this.getInvoiceAmount() == null ? other.getInvoiceAmount() == null : this.getInvoiceAmount().equals(other.getInvoiceAmount()))
            && (this.getBuyerPayAmount() == null ? other.getBuyerPayAmount() == null : this.getBuyerPayAmount().equals(other.getBuyerPayAmount()))
            && (this.getPointAmount() == null ? other.getPointAmount() == null : this.getPointAmount().equals(other.getPointAmount()))
            && (this.getRefundFee() == null ? other.getRefundFee() == null : this.getRefundFee().equals(other.getRefundFee()))
            && (this.getSubject() == null ? other.getSubject() == null : this.getSubject().equals(other.getSubject()))
            && (this.getBody() == null ? other.getBody() == null : this.getBody().equals(other.getBody()))
            && (this.getGmtCreate() == null ? other.getGmtCreate() == null : this.getGmtCreate().equals(other.getGmtCreate()))
            && (this.getGmtPayment() == null ? other.getGmtPayment() == null : this.getGmtPayment().equals(other.getGmtPayment()))
            && (this.getGmtRefund() == null ? other.getGmtRefund() == null : this.getGmtRefund().equals(other.getGmtRefund()))
            && (this.getGmtClose() == null ? other.getGmtClose() == null : this.getGmtClose().equals(other.getGmtClose()))
            && (this.getFundBillList() == null ? other.getFundBillList() == null : this.getFundBillList().equals(other.getFundBillList()))
            && (this.getPassbackParams() == null ? other.getPassbackParams() == null : this.getPassbackParams().equals(other.getPassbackParams()))
            && (this.getVoucherDetailList() == null ? other.getVoucherDetailList() == null : this.getVoucherDetailList().equals(other.getVoucherDetailList()))
            && (this.getAuthAppId() == null ? other.getAuthAppId() == null : this.getAuthAppId().equals(other.getAuthAppId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOutTradeNo() == null) ? 0 : getOutTradeNo().hashCode());
        result = prime * result + ((getTradeNo() == null) ? 0 : getTradeNo().hashCode());
        result = prime * result + ((getOutBizNo() == null) ? 0 : getOutBizNo().hashCode());
        result = prime * result + ((getNotifyTime() == null) ? 0 : getNotifyTime().hashCode());
        result = prime * result + ((getNotifyType() == null) ? 0 : getNotifyType().hashCode());
        result = prime * result + ((getNotifyId() == null) ? 0 : getNotifyId().hashCode());
        result = prime * result + ((getAppId() == null) ? 0 : getAppId().hashCode());
        result = prime * result + ((getCharset() == null) ? 0 : getCharset().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getSignType() == null) ? 0 : getSignType().hashCode());
        result = prime * result + ((getSign() == null) ? 0 : getSign().hashCode());
        result = prime * result + ((getBuyerId() == null) ? 0 : getBuyerId().hashCode());
        result = prime * result + ((getBuyerLogonId() == null) ? 0 : getBuyerLogonId().hashCode());
        result = prime * result + ((getSellerId() == null) ? 0 : getSellerId().hashCode());
        result = prime * result + ((getSellerEmail() == null) ? 0 : getSellerEmail().hashCode());
        result = prime * result + ((getTradeStatus() == null) ? 0 : getTradeStatus().hashCode());
        result = prime * result + ((getTotalAmount() == null) ? 0 : getTotalAmount().hashCode());
        result = prime * result + ((getReceiptAmount() == null) ? 0 : getReceiptAmount().hashCode());
        result = prime * result + ((getInvoiceAmount() == null) ? 0 : getInvoiceAmount().hashCode());
        result = prime * result + ((getBuyerPayAmount() == null) ? 0 : getBuyerPayAmount().hashCode());
        result = prime * result + ((getPointAmount() == null) ? 0 : getPointAmount().hashCode());
        result = prime * result + ((getRefundFee() == null) ? 0 : getRefundFee().hashCode());
        result = prime * result + ((getSubject() == null) ? 0 : getSubject().hashCode());
        result = prime * result + ((getBody() == null) ? 0 : getBody().hashCode());
        result = prime * result + ((getGmtCreate() == null) ? 0 : getGmtCreate().hashCode());
        result = prime * result + ((getGmtPayment() == null) ? 0 : getGmtPayment().hashCode());
        result = prime * result + ((getGmtRefund() == null) ? 0 : getGmtRefund().hashCode());
        result = prime * result + ((getGmtClose() == null) ? 0 : getGmtClose().hashCode());
        result = prime * result + ((getFundBillList() == null) ? 0 : getFundBillList().hashCode());
        result = prime * result + ((getPassbackParams() == null) ? 0 : getPassbackParams().hashCode());
        result = prime * result + ((getVoucherDetailList() == null) ? 0 : getVoucherDetailList().hashCode());
        result = prime * result + ((getAuthAppId() == null) ? 0 : getAuthAppId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", outTradeNo=").append(outTradeNo);
        sb.append(", tradeNo=").append(tradeNo);
        sb.append(", outBizNo=").append(outBizNo);
        sb.append(", notifyTime=").append(notifyTime);
        sb.append(", notifyType=").append(notifyType);
        sb.append(", notifyId=").append(notifyId);
        sb.append(", appId=").append(appId);
        sb.append(", charset=").append(charset);
        sb.append(", version=").append(version);
        sb.append(", signType=").append(signType);
        sb.append(", sign=").append(sign);
        sb.append(", buyerId=").append(buyerId);
        sb.append(", buyerLogonId=").append(buyerLogonId);
        sb.append(", sellerId=").append(sellerId);
        sb.append(", sellerEmail=").append(sellerEmail);
        sb.append(", tradeStatus=").append(tradeStatus);
        sb.append(", totalAmount=").append(totalAmount);
        sb.append(", receiptAmount=").append(receiptAmount);
        sb.append(", invoiceAmount=").append(invoiceAmount);
        sb.append(", buyerPayAmount=").append(buyerPayAmount);
        sb.append(", pointAmount=").append(pointAmount);
        sb.append(", refundFee=").append(refundFee);
        sb.append(", subject=").append(subject);
        sb.append(", body=").append(body);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", gmtPayment=").append(gmtPayment);
        sb.append(", gmtRefund=").append(gmtRefund);
        sb.append(", gmtClose=").append(gmtClose);
        sb.append(", fundBillList=").append(fundBillList);
        sb.append(", passbackParams=").append(passbackParams);
        sb.append(", voucherDetailList=").append(voucherDetailList);
        sb.append(", authAppId=").append(authAppId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}