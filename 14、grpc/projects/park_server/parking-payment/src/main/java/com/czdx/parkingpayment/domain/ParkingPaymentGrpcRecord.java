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
 * 支付系统grpc请求记录
 * @TableName t_parking_payment_grpc_record
 */
@TableName(value ="t_parking_payment_grpc_record")
@Data
public class ParkingPaymentGrpcRecord implements Serializable {
    /**
     * 逻辑ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商户内部订单号
     */
    @TableField(value = "out_trade_no")
    private String outTradeNo;

    /**
     * 第三方交易号
     */
    @TableField(value = "trade_no")
    private String tradeNo;

    /**
     * 订单总金额，单位为元
     */
    @TableField(value = "total_amount")
    private BigDecimal totalAmount;

    /**
     * 退款金额，单位为元
     */
    @TableField(value = "refund_amount")
    private BigDecimal refundAmount;

    /**
     * 支付方式
     */
    @TableField(value = "type")
    private String type;

    /**
     * 调用方法
     */
    @TableField(value = "method")
    private String method;

    /**
     * 请求信息
     */
    @TableField(value = "request_message")
    private String requestMessage;

    /**
     * 响应信息
     */
    @TableField(value = "response_message")
    private String responseMessage;

    /**
     * 请求状态
     */
    @TableField(value = "status")
    private String status;

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
        ParkingPaymentGrpcRecord other = (ParkingPaymentGrpcRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOutTradeNo() == null ? other.getOutTradeNo() == null : this.getOutTradeNo().equals(other.getOutTradeNo()))
            && (this.getTradeNo() == null ? other.getTradeNo() == null : this.getTradeNo().equals(other.getTradeNo()))
            && (this.getTotalAmount() == null ? other.getTotalAmount() == null : this.getTotalAmount().equals(other.getTotalAmount()))
            && (this.getRefundAmount() == null ? other.getRefundAmount() == null : this.getRefundAmount().equals(other.getRefundAmount()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getMethod() == null ? other.getMethod() == null : this.getMethod().equals(other.getMethod()))
            && (this.getRequestMessage() == null ? other.getRequestMessage() == null : this.getRequestMessage().equals(other.getRequestMessage()))
            && (this.getResponseMessage() == null ? other.getResponseMessage() == null : this.getResponseMessage().equals(other.getResponseMessage()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
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
        result = prime * result + ((getTotalAmount() == null) ? 0 : getTotalAmount().hashCode());
        result = prime * result + ((getRefundAmount() == null) ? 0 : getRefundAmount().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getMethod() == null) ? 0 : getMethod().hashCode());
        result = prime * result + ((getRequestMessage() == null) ? 0 : getRequestMessage().hashCode());
        result = prime * result + ((getResponseMessage() == null) ? 0 : getResponseMessage().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
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
        sb.append(", totalAmount=").append(totalAmount);
        sb.append(", refundAmount=").append(refundAmount);
        sb.append(", type=").append(type);
        sb.append(", method=").append(method);
        sb.append(", requestMessage=").append(requestMessage);
        sb.append(", responseMessage=").append(responseMessage);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}