package com.ruoyi.project.parking.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 通道固定车类型绑定表;
 * @author : http://www.chiner.pro
 * @date : 2023-2-21
 */
@ApiModel(value = "通道固定车类型绑定表",description = "")
@TableName("b_passage_car")
@Data
public class BPassageCar implements Serializable,Cloneable{
    /**  */
    @ApiModelProperty(value = "",notes = "null")
    @TableId
    private Integer id ;
    /** 通道id主键 */
    @ApiModelProperty(value = "通道id主键",notes = "")
    private Integer passageId ;
    /** 固定车类型 */
    @ApiModelProperty(value = "固定车类型",notes = "")
    private String ownerCarType ;
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
