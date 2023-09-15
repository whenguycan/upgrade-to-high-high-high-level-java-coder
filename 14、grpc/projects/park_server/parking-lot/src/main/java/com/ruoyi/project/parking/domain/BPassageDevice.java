package com.ruoyi.project.parking.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 通道设备绑定表;
 * @author : http://www.chiner.pro
 * @date : 2023-2-21
 */
@ApiModel(value = "通道设备绑定表",description = "")
@TableName("b_passage_device")
@Data
public class BPassageDevice implements Serializable,Cloneable{
    /**  */
    @ApiModelProperty(value = "",notes = "null")
    @TableId (value = "id", type = IdType.AUTO)
    private Integer id ;
    /** 设备编号 */
    @ApiModelProperty(value = "设备编号",notes = "")
    private String deviceId ;
    /** ip地址 */
    @ApiModelProperty(value = "ip地址",notes = "")
    private String serverIp ;
    /** 软件版本 */
    @ApiModelProperty(value = "软件版本",notes = "")
    private String softVer ;
    /** 设备类型【字典】 */
    @ApiModelProperty(value = "设备类型【字典】",notes = "")
    private String deviceType ;
    /** 通道id主键 */
    @ApiModelProperty(value = "通道id主键",notes = "")
    private Integer passageId ;
    /** 设备状态；'0'-未绑定;'1'-已绑定 */
    @ApiModelProperty(value = "设备状态；'0'-未绑定",notes = "'1'-已绑定")
    private String deviceStatus ;
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
