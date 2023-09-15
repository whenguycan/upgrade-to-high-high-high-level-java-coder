package com.ruoyi.project.parking.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 停车订单
 * 订单系统 返回值对象
 */
@Data
public class VehicleParkOrderVO {

    /**
     * 在场订单id
     */
    private Integer parkLiveId;


    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 场库编号
     */
    private String parkNo;

    /**
     * 场库名称
     */
    private String parkName;

    /**
     * 通道编号
     */
    private String passageNo;

    /**
     * 车牌号
     */
    private String carNumber;


    /**
     * 车型编码
     */
    private String carTypeCode;

    /**
     * 入场时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime entryTime;

    /**
     * 应付金额（元）
     */
    private Double payableAmount;

    /**
     * 优惠金额（元）
     */
    private Double discountAmount;

    /**
     * 已付金额（元）
     */
    private Double paidAmount;

    /**
     * 实付金额（元）
     */
    private Double payAmount;

    /**
     * 支付方式
     */
    private String payMethod;

    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

    /**
     * 支付状态
     */
    private String payStatus;

    /**
     * 支付流水号
     */
    private String payNumber;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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

}
