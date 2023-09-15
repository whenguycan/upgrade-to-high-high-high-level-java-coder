package com.czdx.parkingorder.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: tangwei
 * @Date: 2023/4/10 5:18 PM
 * @Description: 类描述信息
 */
@Data
@TableName("t_parking_bill")
public class ParkingBillEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String type;
    private String buyerName;
    private String buyerTaxNum;
    private String buyerPhone;
    private String buyerEmail;
    private String buyerAddress;
    private String buyerDepositBank;
    private String buyerDepositAccount;
    private String billNo;
    private BigDecimal billAmount;
    private Date billCreateTime;
    private Integer billType;
    private String billOrderNos;
    private String billOutTradeNo;
    private String billPdfUrl;
    private String billStatus;
    private Integer userId;
}
