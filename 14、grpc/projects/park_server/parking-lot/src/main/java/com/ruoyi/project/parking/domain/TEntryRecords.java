package com.ruoyi.project.parking.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 车辆进场记录对象 t_entry_records
 *
 * @author mzl
 * @date 2023-02-22
 */
@ApiModel(value = "车辆进场记录",description = "")
@Data
@TableName("t_entry_records")
public class TEntryRecords implements Serializable
{
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /** 停车场编号 */
    @Excel(name = "停车场编号")
    @ApiModelProperty(value = "停车场编号")
    private String parkNo;
    /** 通道id */
    @Excel(name = "通道id")
    @ApiModelProperty(value = "通道id")
    private Integer passageId;
    /** 通道绑定设备编号 */
    @Excel(name = "通道绑定设备编号")
    @ApiModelProperty(value = "通道绑定设备编号")
    private String deviceId;
    /** 入场时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "入场时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "入场时间")
    private Date entryTime;
    /** 车牌号 */
    @Excel(name = "车牌号")
    @ApiModelProperty(value = "车牌号")
    private String carNumber;
    /** 车牌颜色【字典】 */
    @Excel(name = "车牌颜色【字典】")
    @ApiModelProperty(value = "车牌颜色【字典】")
    private String carNumberColor;
    /** 进场图片存储路径 */
    @Excel(name = "进场图片存储路径")
    @ApiModelProperty(value = "进场图片存储路径")
    private String carImgUrl;
    /** 车身颜色【字典】 */
    @Excel(name = "车身颜色【字典】")
    @ApiModelProperty(value = "车身颜色【字典】")
    private String carColor;
    /** 车型【字典】 */
    @Excel(name = "车型【字典】")
    @ApiModelProperty(value = "车型【字典】")
    private String carType;
    /** 云平台分配的唯一编码 */
    @Excel(name = "云平台分配的唯一编码")
    @ApiModelProperty(value = "云平台分配的唯一编码")
    private String platformCode;
    /** 车牌号 */
    @Excel(name = "车牌号_修改")
    @ApiModelProperty(value = "车牌号_修改")
    private String carNumberEdit;

    private String remark;

    /**
     * 在场记录
     */
    @Excel(name = "在场记录")
    @ApiModelProperty(value = "在场记录")
    private Integer parkLiveId;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
