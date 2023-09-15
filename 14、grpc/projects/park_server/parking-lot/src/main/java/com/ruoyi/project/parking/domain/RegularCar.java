package com.ruoyi.project.parking.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 固定车记录表
 * @TableName t_regular_car
 */
@TableName(value ="t_regular_car")
@Data
public class RegularCar implements Serializable {
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
     * 固定车车牌号
     */
    @TableField(value = "car_number")
    private String carNumber;

    /**
     * 固定车车辆颜色
     */
    @TableField(value = "car_color")
    private String carColor;

    /**
     * 固定车车牌备注
     */
    @TableField(value = "car_remark")
    private String carRemark;

    /**
     * 固定车添加类型 '0'-线下 '1'-线上
     */
    @TableField(value = "car_type")
    private String carType;

    /**
     * 固定车类型
     */
    @TableField(value = "car_category_id")
    private Integer carCategoryId;

    /**
     * 固定车车主身份证号
     */
    @TableField(value = "owner_card_id")
    private String ownerCardId;

    /**
     * 固定车车主姓名
     */
    @TableField(value = "owner_name")
    private String ownerName;

    /**
     * 固定车车主联系地址
     */
    @TableField(value = "owner_address")
    private String ownerAddress;

    /**
     * 固定车车主联系电话
     */
    @TableField(value = "owner_phone")
    private String ownerPhone;

    /**
     * 流动车位数
     */
    @TableField(value = "flow_place_number")
    private Integer flowPlaceNumber;

    /**
     * 有效期开始时间
     */
    @TableField(value = "start_time")
    private LocalDate startTime;

    /**
     * 有效期结束时间
     */
    @TableField(value = "end_time")
    private LocalDate endTime;

    /**
     * 永久时限  '0'-临时 '1'-永久
     */
    @TableField(value = "time_limit")
    private String timeLimit;

    /**
     * 状态  '0'-启用 '1'-停用
     */
    @TableField(value = "status")
    private String status;

    /**
     * 删除标记 0正常 1删除
     */
    @TableField(value = "del_flag")
    private Integer delFlag;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

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
        RegularCar other = (RegularCar) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getParkNo() == null ? other.getParkNo() == null : this.getParkNo().equals(other.getParkNo()))
            && (this.getCarNumber() == null ? other.getCarNumber() == null : this.getCarNumber().equals(other.getCarNumber()))
            && (this.getCarColor() == null ? other.getCarColor() == null : this.getCarColor().equals(other.getCarColor()))
            && (this.getCarRemark() == null ? other.getCarRemark() == null : this.getCarRemark().equals(other.getCarRemark()))
            && (this.getCarType() == null ? other.getCarType() == null : this.getCarType().equals(other.getCarType()))
            && (this.getCarCategoryId() == null ? other.getCarCategoryId() == null : this.getCarCategoryId().equals(other.getCarCategoryId()))
            && (this.getOwnerCardId() == null ? other.getOwnerCardId() == null : this.getOwnerCardId().equals(other.getOwnerCardId()))
            && (this.getOwnerName() == null ? other.getOwnerName() == null : this.getOwnerName().equals(other.getOwnerName()))
            && (this.getOwnerAddress() == null ? other.getOwnerAddress() == null : this.getOwnerAddress().equals(other.getOwnerAddress()))
            && (this.getOwnerPhone() == null ? other.getOwnerPhone() == null : this.getOwnerPhone().equals(other.getOwnerPhone()))
            && (this.getFlowPlaceNumber() == null ? other.getFlowPlaceNumber() == null : this.getFlowPlaceNumber().equals(other.getFlowPlaceNumber()))
            && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getTimeLimit() == null ? other.getTimeLimit() == null : this.getTimeLimit().equals(other.getTimeLimit()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getDelFlag() == null ? other.getDelFlag() == null : this.getDelFlag().equals(other.getDelFlag()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
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
        result = prime * result + ((getCarNumber() == null) ? 0 : getCarNumber().hashCode());
        result = prime * result + ((getCarColor() == null) ? 0 : getCarColor().hashCode());
        result = prime * result + ((getCarRemark() == null) ? 0 : getCarRemark().hashCode());
        result = prime * result + ((getCarType() == null) ? 0 : getCarType().hashCode());
        result = prime * result + ((getCarCategoryId() == null) ? 0 : getCarCategoryId().hashCode());
        result = prime * result + ((getOwnerCardId() == null) ? 0 : getOwnerCardId().hashCode());
        result = prime * result + ((getOwnerName() == null) ? 0 : getOwnerName().hashCode());
        result = prime * result + ((getOwnerAddress() == null) ? 0 : getOwnerAddress().hashCode());
        result = prime * result + ((getOwnerPhone() == null) ? 0 : getOwnerPhone().hashCode());
        result = prime * result + ((getFlowPlaceNumber() == null) ? 0 : getFlowPlaceNumber().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getTimeLimit() == null) ? 0 : getTimeLimit().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getDelFlag() == null) ? 0 : getDelFlag().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
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
        sb.append(", carNumber=").append(carNumber);
        sb.append(", carColor=").append(carColor);
        sb.append(", carRemark=").append(carRemark);
        sb.append(", carType=").append(carType);
        sb.append(", carCategoryId=").append(carCategoryId);
        sb.append(", ownerCardId=").append(ownerCardId);
        sb.append(", ownerName=").append(ownerName);
        sb.append(", ownerAddress=").append(ownerAddress);
        sb.append(", ownerPhone=").append(ownerPhone);
        sb.append(", flowPlaceNumber=").append(flowPlaceNumber);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", timeLimit=").append(timeLimit);
        sb.append(", status=").append(status);
        sb.append(", delFlag=").append(delFlag);
        sb.append(", remark=").append(remark);
        sb.append(", createBy=").append(createBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}