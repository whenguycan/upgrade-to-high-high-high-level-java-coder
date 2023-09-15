package com.czdx.parkingorder.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("t_refund_order")
@Data
public class RefundOrderEntity {


    @TableId(type= IdType.AUTO)
    private Integer id;

    private String orderNo; // '订单号'
    private String orderType; // '订单类型（字典表）'
    private String refundNo; // '订单号'
    private String parkNo; // '所属车场编号'
    private BigDecimal payableAmount; // '应付金额（元）',
    private BigDecimal discountAmount; // '优惠金额（元）',
    private BigDecimal paidAmount; //  '已付金额（元）',
    private BigDecimal payAmount; // '实付金额（元）',
    private BigDecimal refundAmount; // '退款金额（元）',
    private String payMethod; // '支付方式（字典表）',
    private Date payTime; // '支付时间',
    private Date refundTime; // '支付时间',
    private String payNumber; // '支付流水号',
    private String refundStatus; // '退款状态 0-退款中 1-退款成功 -1-退款失败',
    private String reason; // '备注',
    private String remark; // '备注',
    private String createBy; // '创建人',
    private Date createTime; // '创建时间',
    private Date updateTime; // '更新时间',
}
