package com.ruoyi.project.parking.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 场库基础配置表;
 * @author : http://www.chiner.pro
 * @date : 2023-3-14
 */
@Data
@ApiModel(value = "场库基础配置表",description = "")
@TableName("b_setting_lot")
public class BSettingLot implements Serializable,Cloneable{
    /**  */
    @ApiModelProperty(name = "",notes = "null")
    @TableId (value = "id", type = IdType.AUTO)
    private Integer id ;
    /** 场库编号 */
    @ApiModelProperty(name = "场库编号",notes = "")
    private String parkNo ;
    /** 余位为0是否允许车辆入场；0-否；1是 */
    @ApiModelProperty(name = "余位为0是否允许车辆入场；0-否；1是",notes = "")
    private String isPermitEntry ;
    /** 月租车是否占用车位；0-否；1是 */
    @ApiModelProperty(name = "月租车是否占用车位；0-否；1是",notes = "")
    private String isUseLot ;
    /** 场库绑定车类型，逗号隔开 */
    @ApiModelProperty(name = "场库绑定车类型，逗号隔开",notes = "")
    private String bandCarTypes;
    /** 创建者 */
    @ApiModelProperty(name = "创建者",notes = "")
    private String createBy ;
    /** 创建时间 */
    @ApiModelProperty(name = "创建时间",notes = "")
    private Date createTime ;
    /** 更新者 */
    @ApiModelProperty(name = "更新者",notes = "")
    private String updateBy ;
    /** 更新时间 */
    @ApiModelProperty(name = "更新时间",notes = "")
    private Date updateTime ;
}
