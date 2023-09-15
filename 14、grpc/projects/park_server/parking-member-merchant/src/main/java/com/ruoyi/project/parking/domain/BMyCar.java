package com.ruoyi.project.parking.domain;

import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * H5我的车辆管理对象 b_my_car
 * 
 * @author fangch
 * @date 2023-03-02
 */
@Data
@TableName("b_my_car")
public class BMyCar {

    /** 逻辑ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String carNo;

    /** 绑定时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "绑定时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime validTime;

    /** 是否默认(0-否,1-是) */
    @Excel(name = "是否默认")
    private String defaultFlag;

    /** 创建者 */
    @TableField(value = "create_by")
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /** 更新者 */
    @TableField(value = "update_by")
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;
}
