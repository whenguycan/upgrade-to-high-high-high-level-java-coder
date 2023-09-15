package com.ruoyi.project.parking.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 停车预约申请对象 b_visitor_apply
 * 
 * @author fangch
 * @date 2023-03-07
 */
@Data
@TableName("b_visitor_apply")
public class BVisitorApply {

    /** 逻辑ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 访客码 */
    private String code;

    /** 车场编号 */
    @Excel(name = "车场编号")
    private String parkNo;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phone;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String carNo;

    /** 来访日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "来访日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date day;

    /** 申请时间 */
    @Excel(name = "申请时间")
    private String applyTime;

    /** 来访事由 */
    @Excel(name = "来访事由")
    private String visitReason;

    /** 状态(0-审核中,1-已通过,2-已驳回) */
    @Excel(name = "状态(0-审核中,1-已通过,2-已驳回)")
    private String status;

    /** 驳回理由 */
    @Excel(name = "驳回理由")
    private String rejectReason;

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
