package com.ruoyi.project.parking.domain.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 离场车辆 新增参数
 */
@Data
public class ParkLiveRecordsAddParam {

    /**
     * 场库编号
     */
    @Excel(name = "场库编号")
    private String parkNo;

    /**
     * 车牌号
     */
    @NotBlank(message = "车牌号不为空")
    @Excel(name = "车牌号")
    private String carNumber;

    /**
     * 入场时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "入场时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime entryTime;

    /**
     * 出场时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "出场时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime exitTime;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

}
