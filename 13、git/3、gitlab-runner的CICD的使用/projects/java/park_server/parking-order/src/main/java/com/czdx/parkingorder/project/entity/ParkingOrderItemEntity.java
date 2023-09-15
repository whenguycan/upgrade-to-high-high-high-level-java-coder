package com.czdx.parkingorder.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: tangwei
 * @Date: 2023/2/27 5:10 PM
 * @Description: 类描述信息
 */
@Data
@TableName("t_parking_order_item")
public class ParkingOrderItemEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String orderNo; // '订单号',
    private Integer parkFieldId; // '车场场地ID',
    private Date entryTime; // '入场时间',
    private Date exitTime; // '出场时间',
    private Integer parkingTime; // '停车时长（分钟）',
    private BigDecimal payableAmount; // '应付金额（元）',
    private Date createTime; // '创建时间',
    private Date updateTime; // '更新时间',


}
