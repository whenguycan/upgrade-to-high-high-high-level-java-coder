package com.ruoyi.project.parking.domain.vo;


import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class BSelfPaySchemeVO {

    /**
     * 场库编号
     */
    private String parkNo;

    /**
     * 是否可自主续费 '0'-否 '1'-是
     */
    private String renewStatus;

    /**
     * 系统审核 '0'-否 '1'-是
     */
    private String systemVerify;

    /**
     * 新车主审核 '0'-否 '1'-是
     */
    private String newOwnerVerify;

    /**
     * 老车主审核 '0'-否 '1'-是
     */
    private String oldOwnerVerify;

    /**
     * 最大续费天数
     */
    @Min(value = 0,message = "最大续费周期不能为负")
    private Integer maxRenewDays;

    /**
     * 续费临期天数
     */
    @Min(value = 0,message = "续费临期天数不能为负")
    private Integer renewDeadlineDays;
}
