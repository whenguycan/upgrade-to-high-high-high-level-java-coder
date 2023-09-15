package com.czdx.parkingorder.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Auther: tangwei
 * @Date: 2023/3/1 2:46 PM
 * @Description: 类描述信息
 */
@Data
@TableName("t_parking_order_delayed_message")
public class ParkingOrderDelayedMessageEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String orderNo;

    private Integer status;
}
