package com.ruoyi.project.parking.domain.bo;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarVolumeSheetBO {

    /** 统计日期 */
    @Excel(name = "统计日期")
    private String day;

    /** 时段 */
    @Excel(name = "时段")
    private String timeInterval;

    /** 入场车辆数 */
    @Excel(name = "入场车辆数")
    private Integer entryCount;

    /** 出场车辆数 */
    @Excel(name = "出场车辆数")
    private Integer exitCount;

    /** 差异量 */
    @Excel(name = "差异量")
    private Integer disparity;

    /** 差异率 */
    @Excel(name = "差异率")
    private String disparityRate;
}
