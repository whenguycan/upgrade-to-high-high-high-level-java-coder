package com.ruoyi.project.parking.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 自主缴费表
 * @TableName t_self_pay
 */
@TableName(value ="t_self_pay")
@Data
public class SelfPay implements Serializable {
    /**
     * 
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
     * 车牌号
     */
    @TableField(value = "car_number")
    private String carNumber;

    /**
     * 车主姓名
     */
    @TableField(value = "owner_name")
    private String ownerName;

    /**
     * 车主联系方式
     */
    @TableField(value = "owner_phone")
    private String ownerPhone;

    /**
     * 车主类型 '0'-新 '1'-老
     */
    @TableField(value = "owner_type")
    private String ownerType;

    /**
     * 续费天数
     */
    @TableField(value = "renew_days")
    private Integer renewDays;

    /**
     * 申请人
     */
    @TableField(value = "apply_by")
    private String applyBy;

    /**
     * 申请时间
     */
    @TableField(value = "apply_time")
    private LocalDateTime applyTime;

    /**
     * 状态 '0'-未审核 '1'-审核通过 '2'-审核未通过
     */
    @TableField(value = "status")
    private String status;

    /**
     * 创建者
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新者
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
        SelfPay other = (SelfPay) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getParkNo() == null ? other.getParkNo() == null : this.getParkNo().equals(other.getParkNo()))
            && (this.getRegularCarCategoryId() == null ? other.getRegularCarCategoryId() == null : this.getRegularCarCategoryId().equals(other.getRegularCarCategoryId()))
            && (this.getCarNumber() == null ? other.getCarNumber() == null : this.getCarNumber().equals(other.getCarNumber()))
            && (this.getOwnerName() == null ? other.getOwnerName() == null : this.getOwnerName().equals(other.getOwnerName()))
            && (this.getOwnerPhone() == null ? other.getOwnerPhone() == null : this.getOwnerPhone().equals(other.getOwnerPhone()))
            && (this.getOwnerType() == null ? other.getOwnerType() == null : this.getOwnerType().equals(other.getOwnerType()))
            && (this.getRenewDays() == null ? other.getRenewDays() == null : this.getRenewDays().equals(other.getRenewDays()))
            && (this.getApplyBy() == null ? other.getApplyBy() == null : this.getApplyBy().equals(other.getApplyBy()))
            && (this.getApplyTime() == null ? other.getApplyTime() == null : this.getApplyTime().equals(other.getApplyTime()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
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
        result = prime * result + ((getCarNumber() == null) ? 0 : getCarNumber().hashCode());
        result = prime * result + ((getOwnerName() == null) ? 0 : getOwnerName().hashCode());
        result = prime * result + ((getOwnerPhone() == null) ? 0 : getOwnerPhone().hashCode());
        result = prime * result + ((getOwnerType() == null) ? 0 : getOwnerType().hashCode());
        result = prime * result + ((getRenewDays() == null) ? 0 : getRenewDays().hashCode());
        result = prime * result + ((getApplyBy() == null) ? 0 : getApplyBy().hashCode());
        result = prime * result + ((getApplyTime() == null) ? 0 : getApplyTime().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
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
        sb.append(", carNumber=").append(carNumber);
        sb.append(", ownerName=").append(ownerName);
        sb.append(", ownerPhone=").append(ownerPhone);
        sb.append(", ownerType=").append(ownerType);
        sb.append(", renewDays=").append(renewDays);
        sb.append(", applyBy=").append(applyBy);
        sb.append(", applyTime=").append(applyTime);
        sb.append(", status=").append(status);
        sb.append(", createBy=").append(createBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}