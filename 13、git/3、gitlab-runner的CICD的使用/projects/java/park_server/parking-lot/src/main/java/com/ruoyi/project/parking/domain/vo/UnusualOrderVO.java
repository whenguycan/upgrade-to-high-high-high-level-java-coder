package com.ruoyi.project.parking.domain.vo;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 不寻常的订单
 * 对 异常订单 {@link TAbnormalOrderVO}
 * 抵扣订单 {@link TAbnormalOrderVO}
 * 减免订单 {@link TAbnormalOrderVO} 属性聚合
 */
@Data
public class UnusualOrderVO {

    /**
     * 在场记录id
     */
    private Integer parkLiveId;

    /**
     * 停车场编号
     */
    @Excel(name = "停车场编号")
    private String parkNo;

    /**
     * 订单类型 1停车订单
     */
    @Excel(name = "订单类型")
    private Integer orderType;

    /**
     * 订单号
     */
    @Excel(name = "订单号")
    private String orderNo;

    /**
     * 异常/减免/抵扣类型
     */
    @Excel(name = "类型")
    private Integer unusualType;

    /**
     * 异常/减免/抵扣原因
     */
    @Excel(name = "原因")
    private String unusualReason;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    // region 可视化信息
    /**
     * 车牌号
     */
    @Excel(name = "车牌号")
    private String carNumber;

    /**
     * 车型码
     */
    @Excel(name = "车型码")
    private String carType;

    /**
     * 车型名
     */
    @Excel(name = "车型名称")
    private String carTypeName;

    /**
     * 入场时间
     */
    @Excel(name = "入场时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime entryTime;

    /**
     * 出场时间
     */
    @Excel(name = "出场时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime exitTime;

    /**
     * 停车时长
     */
    @Excel(name = "停车时长")
    private String durationTime;

    /**
     * 应付金额（元）
     */
    @Excel(name = "应付金额（元）")
    private Double payableAmount;

    /**
     * 实付金额（元）
     */
    @Excel(name = "实付金额（元）")
    private Double payAmount;

    // endregion

    /**
     * 设置出场时间时 计算停留时间
     */
    public void setExitTime(LocalDateTime exitTime) {
        this.durationTime = DateUtils.getDatePoor(this.entryTime, exitTime);
        this.exitTime = exitTime;
    }
}
