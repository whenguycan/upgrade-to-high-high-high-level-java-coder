package com.ruoyi.project.parking.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.xss.Xss;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.aspectj.lang.annotation.Excel.ColumnType;
import com.ruoyi.framework.aspectj.lang.annotation.Excel.Type;
import com.ruoyi.framework.aspectj.lang.annotation.Excels;
import com.ruoyi.framework.web.domain.BaseEntity;
import com.ruoyi.project.system.domain.SysDept;
import com.ruoyi.project.system.domain.SysRole;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 会员对象
 * 
 * @author fangch
 */
@Data
public class MemberUser {

    /** 序号 */
    @Excel(name = "序号", cellType = ColumnType.NUMERIC)
    private Long userId;

    /** 昵称 */
    @Excel(name = "昵称")
    private String nickName;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phonenumber;

    /** 绑定车辆数 */
    @Excel(name = "绑定车辆数")
    private String boundNum;

    /** 注册时间 */
    @Excel(name = "注册时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /** 最近登录时间 */
    @Excel(name = "最近登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginDate;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    private String memberType;

}
