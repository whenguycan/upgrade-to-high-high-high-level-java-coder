package com.ruoyi.project.parking.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 车场计费规则期间时段对象 b_park_charge_duration_period
 *
 * @author fangch
 * @date 2023-02-21
 */
@ApiModel(value = "车场计费规则期间时段",description = "")
@Data
public class BParkChargeDurationPeriod1 extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 逻辑ID */
    private Integer id;

    /** 期间ID */
    @Excel(name = "期间ID")
    @ApiModelProperty(value = "期间ID")

    private Integer durationId;

    /** 时长（分钟） */
    @Excel(name = "时长", readConverterExp = "分=钟")
    @ApiModelProperty(value = "${column.comment}")

    private Integer lengthOfTime;

    /** 最小单位时长（分钟） */
    @Excel(name = "最小单位时长", readConverterExp = "分=钟")
    @ApiModelProperty(value = "${column.comment}")

    private Integer minLenghtOfTime;

    /** 费率（元） */
    @Excel(name = "费率", readConverterExp = "元=")
    @ApiModelProperty(value = "${column.comment}")

    private BigDecimal rate;

    /** 排序 */
    @Excel(name = "排序")
    @ApiModelProperty(value = "排序")

    private Integer sort;


}
