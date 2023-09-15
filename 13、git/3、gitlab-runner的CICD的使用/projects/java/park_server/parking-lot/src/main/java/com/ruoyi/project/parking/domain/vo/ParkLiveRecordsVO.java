package com.ruoyi.project.parking.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.project.parking.domain.vo.parkingorder.VehicleParkOrderVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 营运订单
 * 在场车辆记录
 */
@Data
public class ParkLiveRecordsVO {

    /**
     * 在场记录id
     */
    @Excel(name = "id", cellType = Excel.ColumnType.NUMERIC)
    private Integer id;

    /**
     * 车牌号
     */
    @Excel(name = "车牌号")
    private String carNumber;

    /**
     * 车辆状态【1-在场；2-离场；3-异常】
     */
    @Excel(name = "车辆状态码")
    private String carStatus;

    /**
     * 停车场编号
     */
    @Excel(name = "停车场编号")
    private String parkNo;

    /**
     * 车型【字典】
     */
    private String carType;

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
     * 缴费时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "缴费时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 创建者
     */
    @Excel(name = "创建者")
    private String createBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 更新者
     */
    @Excel(name = "更新者")
    private String updateBy;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    // region 业务字段
    /**
     * 车型【字典】
     */
    @Excel(name = "车型")
    private String carTypeName;

    /**
     * 通道id （进口）
     */
    @Excel(name = "通道id（进口）")
    private Integer passageId;

    /**
     * 通道名称 （进口）
     */
    @Excel(name = "通道名称 （进口）")
    private String passageName;

    /**
     * 场地名称（进口）
     */
    @Excel(name = "场地名称（进口）")
    private String fieldNameFrom;

    /**
     * 场地名称（出口）
     */
    @Excel(name = "场地名称（出口）")
    private String fieldNameTo;

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
     * 会员ID
     */
    private String memberId;

    /**
     * 订单号
     */
    List<VehicleParkOrderVO> orderNoList;
}
