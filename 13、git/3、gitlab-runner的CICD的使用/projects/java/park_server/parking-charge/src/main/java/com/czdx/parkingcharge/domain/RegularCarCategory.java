package com.czdx.parkingcharge.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 固定车类型表
 * @TableName b_setting_regular_car_category
 */
@TableName(value ="b_setting_regular_car_category")
@Data
public class RegularCarCategory implements Serializable {
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
     * 固定车类型名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 固定车类型码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 分组类型编号（字典）
     */
    @TableField(value = "group_id")
    private String groupId;

    /**
     * 购买时限  '0'-不限制 '1'-限制
     */
    @TableField(value = "time_limit")
    private String timeLimit;

    /**
     * 购买生效时间
     */
    @TableField(value = "start_time")
    private LocalDateTime startTime;

    /**
     * 购买失效时间
     */
    @TableField(value = "end_time")
    private LocalDateTime endTime;

    /**
     * 是否允许线上购买 0否 1 是
     */
    @TableField(value = "online_flag")
    private Integer onlineFlag;

    /**
     * 状态  '0'-停用 '1'-启用
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