package com.ruoyi.project.parking.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 发票抬头记录表
 * @TableName t_invoice_head
 */
@TableName(value ="t_invoice_head")
@Data
public class TInvoiceHead implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 类型（1-公司；2-个人）
     */
    @TableField(value = "type")
    private String type;

    /**
     * 税号
     */
    @TableField(value = "tax_num")
    private String taxNum;

    /**
     * 注册地址
     */
    @TableField(value = "register_address")
    private String registerAddress;

    /**
     * 注册电话
     */
    @TableField(value = "register_phone")
    private String registerPhone;

    /**
     * 开户银行
     */
    @TableField(value = "deposit_bank")
    private String depositBank;

    /**
     * 开户账号
     */
    @TableField(value = "deposit_account")
    private String depositAccount;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 是否是默认 1 默认
     */
    @TableField(value = "flag_default")
    private Integer flagDefault;

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
        TInvoiceHead other = (TInvoiceHead) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getTaxNum() == null ? other.getTaxNum() == null : this.getTaxNum().equals(other.getTaxNum()))
            && (this.getRegisterAddress() == null ? other.getRegisterAddress() == null : this.getRegisterAddress().equals(other.getRegisterAddress()))
            && (this.getRegisterPhone() == null ? other.getRegisterPhone() == null : this.getRegisterPhone().equals(other.getRegisterPhone()))
            && (this.getDepositBank() == null ? other.getDepositBank() == null : this.getDepositBank().equals(other.getDepositBank()))
            && (this.getDepositAccount() == null ? other.getDepositAccount() == null : this.getDepositAccount().equals(other.getDepositAccount()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getFlagDefault() == null ? other.getFlagDefault() == null : this.getFlagDefault().equals(other.getFlagDefault()))
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
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getTaxNum() == null) ? 0 : getTaxNum().hashCode());
        result = prime * result + ((getRegisterAddress() == null) ? 0 : getRegisterAddress().hashCode());
        result = prime * result + ((getRegisterPhone() == null) ? 0 : getRegisterPhone().hashCode());
        result = prime * result + ((getDepositBank() == null) ? 0 : getDepositBank().hashCode());
        result = prime * result + ((getDepositAccount() == null) ? 0 : getDepositAccount().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getFlagDefault() == null) ? 0 : getFlagDefault().hashCode());
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
        sb.append(", userId=").append(userId);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", taxNum=").append(taxNum);
        sb.append(", registerAddress=").append(registerAddress);
        sb.append(", registerPhone=").append(registerPhone);
        sb.append(", depositBank=").append(depositBank);
        sb.append(", depositAccount=").append(depositAccount);
        sb.append(", remark=").append(remark);
        sb.append(", flagDefault=").append(flagDefault);
        sb.append(", createBy=").append(createBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}