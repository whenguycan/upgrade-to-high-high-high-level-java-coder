package com.ruoyi.project.parking.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 车场配置下发记录对象 t_park_setting_issue_records
 *
 * @author ruoyi
 * @date 2023-03-27
 */
@TableName("t_park_setting_issue_records")
@Data
public class TParkSettingIssueRecords extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Integer id;

    /**
     * 停车场编号
     */
    @ApiModelProperty(value = "停车场编号", notes = "null")
    @Excel(name = "停车场编号")
    private String parkNo;

    /**
     * 停车场名称
     */
    @ApiModelProperty(value = "停车场名称", notes = "null")
    private String deptName;

    /**
     * 下发状态；0-失败；1-成功
     */
    @ApiModelProperty(value = "下发状态；0-失败；1-成功", notes = "null")
    @Excel(name = "下发状态；0-失败；1-成功")
    private String issueStatus;

    /**
     * 下发时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "下发时间", notes = "null")
    @Excel(name = "下发时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date issueTime;


}
