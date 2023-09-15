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
 * 通道信息表;
 *
 * @author : http://www.chiner.pro
 * @date : 2023-2-21
 */
@ApiModel(value = "通道信息表", description = "")
@TableName("b_passage")
@Data
public class BPassage implements Serializable, Cloneable {
    /**
     *
     */
    @ApiModelProperty(value = "", notes = "null")
    @TableId (value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 通道名称
     */
    @ApiModelProperty(value = "通道名称", notes = "")
    private String passageName;

    /**
     * 通道名称
     */
    @ApiModelProperty(value = "场地编号", notes = "")
    private String parkNo;


    /**
     * 通道名称
     */
    @ApiModelProperty(value = "通道编号", notes = "")
    private String passageNo;
    /**
     * 来自区域id
     */
    @ApiModelProperty(value = "来自区域id", notes = "")
    private Integer fromFieldId;
    /**
     * 去往区域id
     */
    @ApiModelProperty(value = "去往区域id", notes = "")
    private Integer toFieldId;
    /**
     * 开闸方式
     */
    @ApiModelProperty(value = "开闸方式", notes = "")
    private String openType;
    /**
     * 通道状态；'0'-已停用，;'1'-已启用
     */
    @ApiModelProperty(value = "通道状态；'0'-已停用，'1'-已启用", notes = "")
    private String passageStatus;

    /**
     * 绑定固定车类型，逗号隔开
     */
    @ApiModelProperty(value = "绑定固定车类型，逗号隔开", notes = "")
    private String bandRegularCodes;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", notes = "")
    private String remark;
    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者", notes = "")
    private String createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", notes = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更新者
     */
    @ApiModelProperty(value = "更新者", notes = "")
    private String updateBy;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间", notes = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 通道标示
     */
    @ApiModelProperty(value = "通道标示；1-出口；2-入口", notes = "")
    private String passageFlag;
}
