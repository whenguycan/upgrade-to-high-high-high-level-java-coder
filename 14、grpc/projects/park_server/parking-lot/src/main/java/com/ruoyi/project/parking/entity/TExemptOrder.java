package com.ruoyi.project.parking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 减免订单记录
 * </p>
 *
 * @author yinwen
 * @since 2023-03-10
 */
@Data
@TableName("t_exempt_order")
public class TExemptOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 在场记录id
     */
    private Integer parkLiveId;

    /**
     * 停车场编号
     */
    private String parkNo;

    /**
     * 订单类型 1停车订单
     */
    private Integer orderType;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 减免类型
     */
    private Integer exemptType;

    /**
     * 减免原因
     */
    private String exemptReason;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getParkLiveId() {
        return parkLiveId;
    }

    public void setParkLiveId(Integer parkLiveId) {
        this.parkLiveId = parkLiveId;
    }
    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public Integer getExemptType() {
        return exemptType;
    }

    public void setExemptType(Integer exemptType) {
        this.exemptType = exemptType;
    }
    public String getExemptReason() {
        return exemptReason;
    }

    public void setExemptReason(String exemptReason) {
        this.exemptReason = exemptReason;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "TExemptOrder{" +
            "id=" + id +
            ", parkLiveId=" + parkLiveId +
            ", orderType=" + orderType +
            ", orderNo=" + orderNo +
            ", exemptType=" + exemptType +
            ", exemptReason=" + exemptReason +
            ", remark=" + remark +
            ", createBy=" + createBy +
            ", createTime=" + createTime +
            ", updateBy=" + updateBy +
            ", updateTime=" + updateTime +
        "}";
    }
}
