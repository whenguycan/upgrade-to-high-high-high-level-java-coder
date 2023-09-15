package com.ruoyi.project.parking.domain.bo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class SelfPayBO {

    /**
     * 车牌号
     */
    @Excel(name = "车牌号")
    private String carNumber;

    /**
     * 车主姓名
     */
    @Excel(name = "车主姓名")
    private String ownerName;

    /**
     * 车主联系方式
     */
    @Excel(name = "车主联系方式")
    private String ownerPhone;

    /**
     * 车主类型 '0'-新 '1'-老
     */
    @Excel(name = "车主类型", readConverterExp = "0=新,1=老")
    private String ownerType;

    /**
     * 续费天数
     */
    @Excel(name = "续费天数")
    private Integer renewDays;

    /**
     * 申请人
     */
    @Excel(name = "申请人")
    private String applyBy;

    /**
     * 申请时间
     */
    @Excel(name = "申请时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    /**
     * 状态 '0'-未审核 '1'-审核通过 '2'-审核未通过
     */
    @Excel(name = "状态", type = Excel.Type.EXPORT)
    private String status;

}
