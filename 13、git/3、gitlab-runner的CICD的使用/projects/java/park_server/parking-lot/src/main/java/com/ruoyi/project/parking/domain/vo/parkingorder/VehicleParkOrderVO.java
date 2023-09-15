package com.ruoyi.project.parking.domain.vo.parkingorder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 停车订单
 * 订单系统 返回值对象
 */
@Data
public class VehicleParkOrderVO implements Serializable {

    /**
     * 在场订单id
     */
    private Integer parkLiveId;


    /**
     * 订单号
     */
    @Excel(name = "订单号")
    private String orderNo;

    /**
     * 场库编号
     */
    @Excel(name = "场库编号")
    private String parkNo;

    /**
     * 场库名称
     */
    @Excel(name = "场库名称")
    private String parkName;

    /**
     * 通道编号
     */
    private String passageNo;

    /**
     * 通道编号名
     */
    private String passageName;

    /**
     * 车牌号
     */
    @Excel(name = "车牌号")
    private String carNumber;


    /**
     * 车型编码
     */
    private String carTypeCode;

    /**
     * 入场时间
     */
    @Excel(name = "入场时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime entryTime;

    /**
     * 应付金额（元）
     */
    @Excel(name = "应付金额（元）")
    private Double payableAmount;

    /**
     * 优惠金额（元）
     */
    @Excel(name = "优惠金额（元）")
    private Double discountAmount;

    /**
     * 已付金额（元）
     */
    @Excel(name = "已付金额（元）")
    private Double paidAmount;

    /**
     * 实付金额（元）
     */
    @Excel(name = "实付金额（元）")
    private Double payAmount;

    /**
     * 支付方式
     */
    @Excel(name = "支付方式", readConverterExp = "1=支付宝,2=微信")
    private String payMethod;

    /**
     * 支付时间
     */
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime payTime;

    /**
     * 支付状态
     */
    @Excel(name = "支付状态")
    private String payStatus;

    /**
     * 支付流水号
     */
    @Excel(name = "支付流水号")
    private String payNumber;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime expireTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 场区 订单详情
     */
    List<VehicleParkOrderItemSubVO> itemList;

    /**
     * 停留时间
     */
    private String durationTime;

    /**
     * 进场图片url
     */
    private String carImgUrlFrom;

    /**
     * 优惠原因
     */
    private String discountReason;
}
