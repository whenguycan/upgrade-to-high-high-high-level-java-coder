package com.czdx.parkingorder.project.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: tangwei
 * @Date: 2023/2/27 11:13 AM
 * @Description: 类描述信息
 */
@Data
public class ParkingOrderDetailVo {

    private Integer id;

    private String orderNo; // '订单号'
    private String orderType; // '订单类型（字典表）'
    private String orderStatus; // '订单状态（字典表）',
    private String orderParam; // '入参'
    private String parkNo; // '所属车场编号'
    private String passageNo; // '岗亭编号',
    private String carNumber; // '车牌号',
    private String carTypeCode; // '车型编码',
    private Date entryTime; // '入场时间',
    private BigDecimal payableAmount; // '应付金额（元）',
    private BigDecimal discountAmount; // '优惠金额（元）',
    private BigDecimal paidAmount; //  '已付金额（元）',
    private BigDecimal payAmount; // '实付金额（元）',
    private String payMethod; // '支付方式（字典表）',
    private Date payTime; // '支付时间',
    private String payStatus; // '支付状态（字典表）',
    private String payNumber; // '支付流水号',
    private Date expireTime; // '过期时间',
    private String remark; // '备注',
    private Date createTime; // '创建时间',
    private Date updateTime; // '更新时间',
}
