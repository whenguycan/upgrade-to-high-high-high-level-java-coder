package com.ruoyi.project.parking.domain.vo.monthlyOrder;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

/**
 * 包月订单
 */
@Data
public class MonthlyOrderVO {
    // 订单号
    @Excel(name = "订单号")
    private String orderNo;
    // 车场编号
    @Excel(name = "车场编号")
    private String parkNo;
    // 所属商家ID
    @Excel(name = "所属商家ID")
    private Integer orderUserId;
    // 车牌号
    @Excel(name = "车牌号")
    private String carNumber;
    // 应付金额（元）
    @Excel(name = "应付金额（元）")
    private Double payableAmount;
    // 优惠金额（元）
    @Excel(name = "优惠金额（元）")
    private Double discountAmount;
    // 已付金额（元）
    @Excel(name = "已付金额（元）")
    private Double paidAmount;
    // 实付金额（元）
    @Excel(name = "实付金额（元）")
    private Double payAmount;
    //  支付方式
    @Excel(name = "支付方式", readConverterExp = "1=支付宝,2=微信")
    private String payMethod;
    // 支付时间
    @Excel(name = "支付时间")
    private String payTime;
    // 支付状态
    @Excel(name = "支付状态")
    private String payStatus;
    // 支付流水号
    @Excel(name = "支付流水号")
    private String payNumber;
    // 过期时间
    private String expireTime;
    // 备注
    private String remark;
    // 创建时间
    private String createTime;
    // 更新时间
    private String updateTime;

    @Excel(name = "订单状态")
    private String orderStatus;
}
