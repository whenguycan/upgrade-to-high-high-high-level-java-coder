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
 * 微信支付回调通知记录表
 * @TableName t_wechatpay_notification
 */
@TableName(value ="t_wechatpay_notification")
@Data
public class WechatpayNotification implements Serializable {
    /**
     * 逻辑ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商户系统内部订单号
     */
    @TableField(value = "out_trade_no")
    private String outTradeNo;

    /**
     * 微信支付系统生成的订单号。
     */
    @TableField(value = "transaction_id")
    private String transactionId;

    /**
     * 商户退款单号
     */
    @TableField(value = "out_refund_no")
    private String outRefundNo;

    /**
     * 微信退款单号
     */
    @TableField(value = "refund_id")
    private String refundId;

    /**
     * 完成时间
     */
    @TableField(value = "success_time")
    private LocalDateTime successTime;

    /**
     * 通知校验 ID
     */
    @TableField(value = "notify_id")
    private String notifyId;

    /**
     * 直连商户申请的公众号或移动应用appid。
     */
    @TableField(value = "app_id")
    private String appId;

    /**
     * 商户的商户号，由微信支付生成并下发。
     */
    @TableField(value = "mch_id")
    private String mchId;

    /**
     * 交易类型
     */
    @TableField(value = "trade_type")
    private String tradeType;

    /**
     * 用户在直连商户appid下的唯一标识。
     */
    @TableField(value = "open_id")
    private String openId;

    /**
     * 退款状态
     */
    @TableField(value = "refund_status")
    private String refundStatus;

    /**
     * 交易状态
     */
    @TableField(value = "trade_state")
    private String tradeState;

    /**
     * 交易状态描述
     */
    @TableField(value = "trade_state_desc")
    private String tradeStateDesc;

    /**
     * 取当前退款单的退款入账方。
     */
    @TableField(value = "user_received_account")
    private String userReceivedAccount;

    /**
     * 银行类型
     */
    @TableField(value = "bank_type")
    private String bankType;

    /**
     * 附加数据，实际情况下只有支付完成状态才会返回该字段。
     */
    @TableField(value = "attach")
    private String attach;

    /**
     * 订单总金额，单位为元
     */
    @TableField(value = "total")
    private BigDecimal total;

    /**
     * 退款金额，单位为元
     */
    @TableField(value = "refund")
    private BigDecimal refund;

    /**
     * 用户支付金额，单位为元
     */
    @TableField(value = "payer_total")
    private BigDecimal payerTotal;

    /**
     * 退款给用户的金额，不包含所有优惠券金额
     */
    @TableField(value = "payer_refund")
    private BigDecimal payerRefund;

    /**
     * CNY：人民币，境内商户号仅支持人民币。
     */
    @TableField(value = "currency")
    private String currency;

    /**
     * 用户支付币种
     */
    @TableField(value = "payer_currency")
    private String payerCurrency;

    /**
     * 商品的标题/交易标题/订单标题/订单关键字等，是请求时对应的参数，原样通知回来。
     */
    @TableField(value = "promotion_detail")
    private String promotionDetail;

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
        WechatpayNotification other = (WechatpayNotification) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOutTradeNo() == null ? other.getOutTradeNo() == null : this.getOutTradeNo().equals(other.getOutTradeNo()))
            && (this.getTransactionId() == null ? other.getTransactionId() == null : this.getTransactionId().equals(other.getTransactionId()))
            && (this.getOutRefundNo() == null ? other.getOutRefundNo() == null : this.getOutRefundNo().equals(other.getOutRefundNo()))
            && (this.getRefundId() == null ? other.getRefundId() == null : this.getRefundId().equals(other.getRefundId()))
            && (this.getSuccessTime() == null ? other.getSuccessTime() == null : this.getSuccessTime().equals(other.getSuccessTime()))
            && (this.getNotifyId() == null ? other.getNotifyId() == null : this.getNotifyId().equals(other.getNotifyId()))
            && (this.getAppId() == null ? other.getAppId() == null : this.getAppId().equals(other.getAppId()))
            && (this.getMchId() == null ? other.getMchId() == null : this.getMchId().equals(other.getMchId()))
            && (this.getTradeType() == null ? other.getTradeType() == null : this.getTradeType().equals(other.getTradeType()))
            && (this.getOpenId() == null ? other.getOpenId() == null : this.getOpenId().equals(other.getOpenId()))
            && (this.getRefundStatus() == null ? other.getRefundStatus() == null : this.getRefundStatus().equals(other.getRefundStatus()))
            && (this.getTradeState() == null ? other.getTradeState() == null : this.getTradeState().equals(other.getTradeState()))
            && (this.getTradeStateDesc() == null ? other.getTradeStateDesc() == null : this.getTradeStateDesc().equals(other.getTradeStateDesc()))
            && (this.getUserReceivedAccount() == null ? other.getUserReceivedAccount() == null : this.getUserReceivedAccount().equals(other.getUserReceivedAccount()))
            && (this.getBankType() == null ? other.getBankType() == null : this.getBankType().equals(other.getBankType()))
            && (this.getAttach() == null ? other.getAttach() == null : this.getAttach().equals(other.getAttach()))
            && (this.getTotal() == null ? other.getTotal() == null : this.getTotal().equals(other.getTotal()))
            && (this.getRefund() == null ? other.getRefund() == null : this.getRefund().equals(other.getRefund()))
            && (this.getPayerTotal() == null ? other.getPayerTotal() == null : this.getPayerTotal().equals(other.getPayerTotal()))
            && (this.getPayerRefund() == null ? other.getPayerRefund() == null : this.getPayerRefund().equals(other.getPayerRefund()))
            && (this.getCurrency() == null ? other.getCurrency() == null : this.getCurrency().equals(other.getCurrency()))
            && (this.getPayerCurrency() == null ? other.getPayerCurrency() == null : this.getPayerCurrency().equals(other.getPayerCurrency()))
            && (this.getPromotionDetail() == null ? other.getPromotionDetail() == null : this.getPromotionDetail().equals(other.getPromotionDetail()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOutTradeNo() == null) ? 0 : getOutTradeNo().hashCode());
        result = prime * result + ((getTransactionId() == null) ? 0 : getTransactionId().hashCode());
        result = prime * result + ((getOutRefundNo() == null) ? 0 : getOutRefundNo().hashCode());
        result = prime * result + ((getRefundId() == null) ? 0 : getRefundId().hashCode());
        result = prime * result + ((getSuccessTime() == null) ? 0 : getSuccessTime().hashCode());
        result = prime * result + ((getNotifyId() == null) ? 0 : getNotifyId().hashCode());
        result = prime * result + ((getAppId() == null) ? 0 : getAppId().hashCode());
        result = prime * result + ((getMchId() == null) ? 0 : getMchId().hashCode());
        result = prime * result + ((getTradeType() == null) ? 0 : getTradeType().hashCode());
        result = prime * result + ((getOpenId() == null) ? 0 : getOpenId().hashCode());
        result = prime * result + ((getRefundStatus() == null) ? 0 : getRefundStatus().hashCode());
        result = prime * result + ((getTradeState() == null) ? 0 : getTradeState().hashCode());
        result = prime * result + ((getTradeStateDesc() == null) ? 0 : getTradeStateDesc().hashCode());
        result = prime * result + ((getUserReceivedAccount() == null) ? 0 : getUserReceivedAccount().hashCode());
        result = prime * result + ((getBankType() == null) ? 0 : getBankType().hashCode());
        result = prime * result + ((getAttach() == null) ? 0 : getAttach().hashCode());
        result = prime * result + ((getTotal() == null) ? 0 : getTotal().hashCode());
        result = prime * result + ((getRefund() == null) ? 0 : getRefund().hashCode());
        result = prime * result + ((getPayerTotal() == null) ? 0 : getPayerTotal().hashCode());
        result = prime * result + ((getPayerRefund() == null) ? 0 : getPayerRefund().hashCode());
        result = prime * result + ((getCurrency() == null) ? 0 : getCurrency().hashCode());
        result = prime * result + ((getPayerCurrency() == null) ? 0 : getPayerCurrency().hashCode());
        result = prime * result + ((getPromotionDetail() == null) ? 0 : getPromotionDetail().hashCode());
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
        sb.append(", transactionId=").append(transactionId);
        sb.append(", outRefundNo=").append(outRefundNo);
        sb.append(", refundId=").append(refundId);
        sb.append(", successTime=").append(successTime);
        sb.append(", notifyId=").append(notifyId);
        sb.append(", appId=").append(appId);
        sb.append(", mchId=").append(mchId);
        sb.append(", tradeType=").append(tradeType);
        sb.append(", openId=").append(openId);
        sb.append(", refundStatus=").append(refundStatus);
        sb.append(", tradeState=").append(tradeState);
        sb.append(", tradeStateDesc=").append(tradeStateDesc);
        sb.append(", userReceivedAccount=").append(userReceivedAccount);
        sb.append(", bankType=").append(bankType);
        sb.append(", attach=").append(attach);
        sb.append(", total=").append(total);
        sb.append(", refund=").append(refund);
        sb.append(", payerTotal=").append(payerTotal);
        sb.append(", payerRefund=").append(payerRefund);
        sb.append(", currency=").append(currency);
        sb.append(", payerCurrency=").append(payerCurrency);
        sb.append(", promotionDetail=").append(promotionDetail);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}