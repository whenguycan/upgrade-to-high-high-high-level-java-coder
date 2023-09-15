package com.ruoyi.project.parking.domain.bo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class WhiteListBO {

    /**
     * 白名单车牌号
     */
    @Excel(name = "白名单车牌号")
    private String carNumber;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    /**
     * 起始时间
     */
    @Excel(name = "起始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 截止时间
     */
    @Excel(name = "截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;


    /**
     * 创建人
     */
    @Excel(name = "创建人", type = Excel.Type.EXPORT)
    private String createBy;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", type = Excel.Type.EXPORT, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
