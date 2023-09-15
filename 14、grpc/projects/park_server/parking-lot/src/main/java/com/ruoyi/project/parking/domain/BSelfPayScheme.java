package com.ruoyi.project.parking.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 自主缴费方案表
 * @TableName b_self_pay_scheme
 */
@TableName(value ="b_self_pay_scheme")
@Data
public class BSelfPayScheme implements Serializable {
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
     * 是否可自主续费 '0'-否 '1'-是
     */
    @TableField(value = "renew_status")
    private String renewStatus;

    /**
     * 系统审核 '0'-否 '1'-是
     */
    @TableField(value = "system_verify")
    private String systemVerify;

    /**
     * 新车主审核 '0'-否 '1'-是
     */
    @TableField(value = "new_owner_verify")
    private String newOwnerVerify;

    /**
     * 老车主审核 '0'-否 '1'-是
     */
    @TableField(value = "old_owner_verify")
    private String oldOwnerVerify;

    /**
     * 最大续费天数
     */
    @TableField(value = "max_renew_days")
    private Integer maxRenewDays;

    /**
     * 续费临期天数
     */
    @TableField(value = "renew_deadline_days")
    private Integer renewDeadlineDays;

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
        BSelfPayScheme other = (BSelfPayScheme) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getParkNo() == null ? other.getParkNo() == null : this.getParkNo().equals(other.getParkNo()))
            && (this.getRenewStatus() == null ? other.getRenewStatus() == null : this.getRenewStatus().equals(other.getRenewStatus()))
            && (this.getSystemVerify() == null ? other.getSystemVerify() == null : this.getSystemVerify().equals(other.getSystemVerify()))
            && (this.getNewOwnerVerify() == null ? other.getNewOwnerVerify() == null : this.getNewOwnerVerify().equals(other.getNewOwnerVerify()))
            && (this.getOldOwnerVerify() == null ? other.getOldOwnerVerify() == null : this.getOldOwnerVerify().equals(other.getOldOwnerVerify()))
            && (this.getMaxRenewDays() == null ? other.getMaxRenewDays() == null : this.getMaxRenewDays().equals(other.getMaxRenewDays()))
            && (this.getRenewDeadlineDays() == null ? other.getRenewDeadlineDays() == null : this.getRenewDeadlineDays().equals(other.getRenewDeadlineDays()))
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
        result = prime * result + ((getRenewStatus() == null) ? 0 : getRenewStatus().hashCode());
        result = prime * result + ((getSystemVerify() == null) ? 0 : getSystemVerify().hashCode());
        result = prime * result + ((getNewOwnerVerify() == null) ? 0 : getNewOwnerVerify().hashCode());
        result = prime * result + ((getOldOwnerVerify() == null) ? 0 : getOldOwnerVerify().hashCode());
        result = prime * result + ((getMaxRenewDays() == null) ? 0 : getMaxRenewDays().hashCode());
        result = prime * result + ((getRenewDeadlineDays() == null) ? 0 : getRenewDeadlineDays().hashCode());
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
        sb.append(", renewStatus=").append(renewStatus);
        sb.append(", systemVerify=").append(systemVerify);
        sb.append(", newOwnerVerify=").append(newOwnerVerify);
        sb.append(", oldOwnerVerify=").append(oldOwnerVerify);
        sb.append(", maxRenewDays=").append(maxRenewDays);
        sb.append(", renewDeadlineDays=").append(renewDeadlineDays);
        sb.append(", createBy=").append(createBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}