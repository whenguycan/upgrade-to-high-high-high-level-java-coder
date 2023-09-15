package com.ruoyi.project.parking.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 起落杆记录对象 t_passage_lever_records
 *
 * @author ruoyi
 * @date 2023-02-23
 */
@ApiModel(value = "起落杆记录",description = "")
@Data
public class TPassageLeverRecords extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;
    /** 操作类型；1-起；2-落；3-停止；4-敞开；5-恢复正常 */
    @Excel(name = "操作类型；1-起；2-落；3-停止；4-敞开；5-恢复正常")
    @ApiModelProperty(value = "操作类型；1-起；2-落；3-停止；4-敞开；5-恢复正常")
    private String operateType;
    /** 场库编号 */
    @Excel(name = "场库编号")
    @ApiModelProperty(value = "场库编号")
    private String parkNo;
    /** 通道编号 */
    @Excel(name = "通道编号")
    @ApiModelProperty(value = "通道编号")
    private String passageNo;

}
