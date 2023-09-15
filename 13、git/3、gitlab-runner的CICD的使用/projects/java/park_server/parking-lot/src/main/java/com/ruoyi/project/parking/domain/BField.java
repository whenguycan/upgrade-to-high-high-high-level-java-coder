package com.ruoyi.project.parking.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

 /**
 * 区域管理表;
 * @author : http://www.chiner.pro
 * @date : 2023-2-21
 */
@ApiModel(value = "区域管理表",description = "")
@TableName("b_field")
@Data
public class BField implements Serializable,Cloneable{
    /**  */
    @ApiModelProperty(value = "id",notes = "null")
    @TableId (value = "id", type = IdType.AUTO)
    private Integer id ;
    /** 场库编号 */
    @ApiModelProperty(value = "场库编号",notes = "")
    private String parkNo ;
    /** 区域名称 */
    @ApiModelProperty(value = "区域名称",notes = "")
    private String fieldName ;
    /** 区域状态；'0'-已停用;，'1'-已启用 */
    @ApiModelProperty(value = "区域状态；'0'-已停用",notes = "，'1'-已启用")
    private String fieldStatus ;
    /** 车位数 */
    @ApiModelProperty(value = "车位数",notes = "")
    private Integer spaceCount ;
    /** 备注 */
    @ApiModelProperty(value = "备注",notes = "")
    private String remark ;
    /** 创建者 */
    @ApiModelProperty(value = "创建者",notes = "")
    private String createBy ;
    /** 创建时间 */
    @ApiModelProperty(value = "创建时间",notes = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime ;
    /** 更新者 */
    @ApiModelProperty(value = "更新者",notes = "")
    private String updateBy ;
    /** 更新时间 */
    @ApiModelProperty(value = "更新时间",notes = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime ;

}
