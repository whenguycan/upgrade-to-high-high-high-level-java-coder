package com.ruoyi.project.parking.domain.bo;


import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class BlackListBO {

    /**
     * 黑名单车牌号
     */
    @Excel(name = "黑名单车牌号")
    private String carNumber;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

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
