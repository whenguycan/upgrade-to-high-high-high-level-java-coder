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
 * 访客码管理对象 b_visitor_code_manage
 * 
 * @author fangch
 * @date 2023-03-06
 */
@Data
@TableName("b_visitor_code_manage")
public class BVisitorCodeManage {
    /** 逻辑ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 车场编号 */
//    @Excel(name = "车场编号")
    private String parkNo;

    /** 访客码名称 */
    @Excel(name = "访客码名称")
    private String codeName;

    /** 绑定次数 */
    @Excel(name = "绑定次数")
    private Integer codeUseNumber;

    /** 已绑定次数 */
    @Excel(name = "已绑定次数")
    private Integer codeUsedNumber;

    /** 免费天数 */
    @Excel(name = "免费天数")
    private Integer codeFreeDay;

    /** 有效时间-开始 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "有效时间-开始", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 有效时间-结束 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "有效时间-结束", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 是否永久有效期(0-否,1-是) */
    @Excel(name = "是否永久有效期", readConverterExp = "0=否,1=是")
    private String timeLimit;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    /** 申请时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "申请时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applyTime;

    /** 码状态(1-启用 2-停用) */
//    @Excel(name = "码状态", readConverterExp = "1=启用,2=停用")
    private String status;

    /** 二维码编号 */
//    @Excel(name = "二维码编号")
    private String code;

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
