package com.ruoyi.project.parking.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 车辆类型表;
 * @author : http://www.chiner.pro
 * @date : 2023-2-21
 */
@ApiModel(value = "车辆类型表",description = "")
@TableName("b_setting_car_type")
@Data
public class BSettingCarType implements Serializable,Cloneable{
    /**  */
    @ApiModelProperty(value = "",notes = "null")
    @TableId
    private Integer id ;
    /** 场库编号 */
    @ApiModelProperty(value = "场库编号",notes = "")
    private String parkNo ;
    /** 类型名称 */
    @ApiModelProperty(value = "类型名称",notes = "")
    private String name ;
    /** 车辆类型码 */
    @ApiModelProperty(value = "车辆类型码",notes = "")
    private String code ;
    /** 删除标记;1删除 0正常 */
    @ApiModelProperty(value = "删除标记",notes = "1删除 0正常")
    private Integer delFlag ;
    /** 备注 */
    @ApiModelProperty(value = "备注",notes = "")
    private String remark ;
    /** 创建者 */
    @ApiModelProperty(value = "创建者",notes = "")
    private String createBy ;
    /** 创建时间 */
    @ApiModelProperty(value = "创建时间",notes = "")
    private Date createTime ;
    /** 更新者 */
    @ApiModelProperty(value = "更新者",notes = "")
    private String updateBy ;
    /** 更新时间 */
    @ApiModelProperty(value = "更新时间",notes = "")
    private Date updateTime ;

}
