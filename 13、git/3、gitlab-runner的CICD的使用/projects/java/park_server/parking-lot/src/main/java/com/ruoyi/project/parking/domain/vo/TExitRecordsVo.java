package com.ruoyi.project.parking.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.project.parking.domain.TExitRecords;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class TExitRecordsVo extends TExitRecords {

    /** 区域名称 */
    @ApiModelProperty(value = "区域名称",notes = "")
    private String fieldName ;
    /**
     * 通道名称
     */
    @ApiModelProperty(value = "通道名称", notes = "")
    private String passageName;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", notes = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginDate;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", notes = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    @ApiModelProperty(value = "付款金额", notes = "")
    private Double payAmount;

    @ApiModelProperty(value = "放行理由", notes = "")
    private String liftGateReason;
}
