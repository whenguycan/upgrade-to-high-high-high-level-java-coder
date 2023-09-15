package com.czdx.parkingcharge.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 固定车记录表
 * @TableName t_regular_car
 */
@TableName(value ="t_regular_car")
@Data
public class RegularCar implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 场库编号
     */
    @TableField(value = "park_no")
    private String parkNo;

    /**
     * 固定车车牌号
     */
    @TableField(value = "car_number")
    private String carNumber;

    /**
     * 固定车车辆颜色
     */
    @TableField(value = "car_color")
    private String carColor;

    /**
     * 固定车车牌备注
     */
    @TableField(value = "car_remark")
    private String carRemark;

    /**
     * 固定车添加类型 '0'-线下 '1'-线上
     */
    @TableField(value = "car_type")
    private String carType;

    /**
     * 固定车类型
     */
    @TableField(value = "car_category_id")
    private Integer carCategoryId;

    /**
     * 固定车车主身份证号
     */
    @TableField(value = "owner_card_id")
    private String ownerCardId;

    /**
     * 固定车车主姓名
     */
    @TableField(value = "owner_name")
    private String ownerName;

    /**
     * 固定车车主联系地址
     */
    @TableField(value = "owner_address")
    private String ownerAddress;

    /**
     * 固定车车主联系电话
     */
    @TableField(value = "owner_phone")
    private String ownerPhone;

    /**
     * 流动车位数
     */
    @TableField(value = "flow_place_number")
    private Integer flowPlaceNumber;

    /**
     * 有效期开始时间
     */
    @TableField(value = "start_time")
    private LocalDate startTime;

    /**
     * 有效期结束时间
     */
    @TableField(value = "end_time")
    private LocalDate endTime;

    /**
     * 永久时限  '0'-临时 '1'-永久
     */
    @TableField(value = "time_limit")
    private String timeLimit;

    /**
     * 状态  '0'-启用 '1'-停用
     */
    @TableField(value = "status")
    private String status;

    /**
     * 删除标记 0正常 1删除
     */
    @TableField(value = "del_flag")
    private Integer delFlag;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 创建者
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    @TableField(value = "update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}