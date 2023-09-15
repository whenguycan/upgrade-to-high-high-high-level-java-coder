package com.ruoyi.project.parking.domain.vo;

import com.ruoyi.project.parking.domain.TPassageLeverRecords;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "起落杆记录",description = "")
@Data
public class TPassageLeverRecordsVo extends TPassageLeverRecords {
    @ApiModelProperty(value = "通道编号")
    private String passageName;
    @ApiModelProperty(value = "场库名称")
    private String parkName;
}
