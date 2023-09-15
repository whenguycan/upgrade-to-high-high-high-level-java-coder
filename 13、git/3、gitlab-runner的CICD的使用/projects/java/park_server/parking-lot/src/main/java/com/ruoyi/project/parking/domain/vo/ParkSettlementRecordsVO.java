package com.ruoyi.project.parking.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.project.parking.domain.vo.parkingorder.VehicleParkOrderVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 离场车辆 记录
 */
@Data
public class ParkSettlementRecordsVO {
    /**
     * 在场记录id
     */
    private Integer id;

    /**
     * 车牌号
     */
    @Excel(name = "车牌号")
    private String carNumber;

    /**
     * 车型【字典】
     */
    private String carType;

    /**
     * 车型【字典】
     */
    @Excel(name = "车型")
    private String carTypeName;

    /**
     * 入场时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "入场时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime entryTime;

    /**
     * 出场时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "出场时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime exitTime;

    /**
     * 停留时长
     */
    @Excel(name = "停留时长")
    private String durationTime;

    /**
     * 停车费用
     */
    @Excel(name = "停车费用")
    private Double parkingFee;

    /**
     * 备注
     */
    private String remark;

    /**
     * 进场图片存储路径
     */
    @Excel(name = "进场图片存储路径")
    private String carImgUrlFrom;

    /**
     * 出场图片存储路径
     */
    @Excel(name = "出场图片存储路径")
    private String carImgUrlTo;

    /**
     * 关联订单信息
     */
    List<VehicleParkOrderVO> orderNoList;

    public String getDurationTime() {
        return DateUtils.getDatePoor(this.entryTime, this.exitTime);
    }

    public Double getParkingFee() {
        // 已完成的订单 实付和
        return this.orderNoList.stream()
                .filter(m -> "04".equals(m.getPayStatus()))
                .mapToDouble(VehicleParkOrderVO::getPayAmount).sum();

    }
}
