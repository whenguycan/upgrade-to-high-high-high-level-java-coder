package com.ruoyi.project.merchant.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * H5月租车订单表
 * @TableName t_monthly_car_rental_order
 */
@TableName(value ="t_monthly_car_rental_order")
@Data
public class MonthlyCarRentalOrder implements Serializable {
    /**
     * 逻辑ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 场库编号
     */
    @TableField(value = "park_no")
    private String parkNo;

    /**
     * 固定车类型id
     */
    @TableField(value = "regular_car_category_id")
    private Integer regularCarCategoryId;

    /**
     * 固定车价格id
     */
    @TableField(value = "regular_car_category_price_id")
    private Integer regularCarCategoryPriceId;

    /**
     * 续费天数
     */
    @TableField(value = "rental_days")
    private Integer rentalDays;

    /**
     * 续费价格
     */
    @TableField(value = "rental_price")
    private BigDecimal rentalPrice;

    /**
     * 车牌号
     */
    @TableField(value = "car_number")
    private String carNumber;

    /**
     * 下单用户id
     */
    @TableField(value = "order_user_id")
    private Integer orderUserId;

    /**
     * 订单号
     */
    @TableField(value = "order_no")
    private String orderNo;

    /**
     * 应付金额（元）
     */
    @TableField(value = "payable_amount")
    private BigDecimal payableAmount;

    /**
     * 优惠金额（元）
     */
    @TableField(value = "discount_amount")
    private BigDecimal discountAmount;

    /**
     * 已付金额（元）
     */
    @TableField(value = "paid_amount")
    private BigDecimal paidAmount;

    /**
     * 实付金额（元）
     */
    @TableField(value = "pay_amount")
    private BigDecimal payAmount;

    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(value = "update_by")
    private String updateBy;

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
        MonthlyCarRentalOrder other = (MonthlyCarRentalOrder) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getParkNo() == null ? other.getParkNo() == null : this.getParkNo().equals(other.getParkNo()))
            && (this.getRegularCarCategoryId() == null ? other.getRegularCarCategoryId() == null : this.getRegularCarCategoryId().equals(other.getRegularCarCategoryId()))
            && (this.getRegularCarCategoryPriceId() == null ? other.getRegularCarCategoryPriceId() == null : this.getRegularCarCategoryPriceId().equals(other.getRegularCarCategoryPriceId()))
            && (this.getRentalDays() == null ? other.getRentalDays() == null : this.getRentalDays().equals(other.getRentalDays()))
            && (this.getRentalPrice() == null ? other.getRentalPrice() == null : this.getRentalPrice().equals(other.getRentalPrice()))
            && (this.getCarNumber() == null ? other.getCarNumber() == null : this.getCarNumber().equals(other.getCarNumber()))
            && (this.getOrderUserId() == null ? other.getOrderUserId() == null : this.getOrderUserId().equals(other.getOrderUserId()))
            && (this.getOrderNo() == null ? other.getOrderNo() == null : this.getOrderNo().equals(other.getOrderNo()))
            && (this.getPayableAmount() == null ? other.getPayableAmount() == null : this.getPayableAmount().equals(other.getPayableAmount()))
            && (this.getDiscountAmount() == null ? other.getDiscountAmount() == null : this.getDiscountAmount().equals(other.getDiscountAmount()))
            && (this.getPaidAmount() == null ? other.getPaidAmount() == null : this.getPaidAmount().equals(other.getPaidAmount()))
            && (this.getPayAmount() == null ? other.getPayAmount() == null : this.getPayAmount().equals(other.getPayAmount()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getParkNo() == null) ? 0 : getParkNo().hashCode());
        result = prime * result + ((getRegularCarCategoryId() == null) ? 0 : getRegularCarCategoryId().hashCode());
        result = prime * result + ((getRegularCarCategoryPriceId() == null) ? 0 : getRegularCarCategoryPriceId().hashCode());
        result = prime * result + ((getRentalDays() == null) ? 0 : getRentalDays().hashCode());
        result = prime * result + ((getRentalPrice() == null) ? 0 : getRentalPrice().hashCode());
        result = prime * result + ((getCarNumber() == null) ? 0 : getCarNumber().hashCode());
        result = prime * result + ((getOrderUserId() == null) ? 0 : getOrderUserId().hashCode());
        result = prime * result + ((getOrderNo() == null) ? 0 : getOrderNo().hashCode());
        result = prime * result + ((getPayableAmount() == null) ? 0 : getPayableAmount().hashCode());
        result = prime * result + ((getDiscountAmount() == null) ? 0 : getDiscountAmount().hashCode());
        result = prime * result + ((getPaidAmount() == null) ? 0 : getPaidAmount().hashCode());
        result = prime * result + ((getPayAmount() == null) ? 0 : getPayAmount().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
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
        sb.append(", parkNo=").append(parkNo);
        sb.append(", regularCarCategoryId=").append(regularCarCategoryId);
        sb.append(", regularCarCategoryPriceId=").append(regularCarCategoryPriceId);
        sb.append(", rentalDays=").append(rentalDays);
        sb.append(", rentalPrice=").append(rentalPrice);
        sb.append(", carNumber=").append(carNumber);
        sb.append(", orderUserId=").append(orderUserId);
        sb.append(", orderNo=").append(orderNo);
        sb.append(", payableAmount=").append(payableAmount);
        sb.append(", discountAmount=").append(discountAmount);
        sb.append(", paidAmount=").append(paidAmount);
        sb.append(", payAmount=").append(payAmount);
        sb.append(", createBy=").append(createBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}