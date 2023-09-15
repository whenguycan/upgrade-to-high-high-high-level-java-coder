package com.ruoyi.project.parking.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.aspectj.lang.annotation.Excel.ColumnType;
import com.ruoyi.framework.aspectj.lang.annotation.Excel.Type;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会员对象
 * 
 * @author fangch
 */
@Data
public class MemberUserVO {

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
