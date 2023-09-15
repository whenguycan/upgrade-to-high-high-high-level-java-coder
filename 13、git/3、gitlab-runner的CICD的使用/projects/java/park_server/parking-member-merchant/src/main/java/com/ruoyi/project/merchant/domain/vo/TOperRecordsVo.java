package com.ruoyi.project.merchant.domain.vo;

import com.ruoyi.project.merchant.domain.TOperRecords;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "订单流水记录", description = "")
@Data
public class TOperRecordsVo extends TOperRecords {
    private String beginTime;
    private String endTime;
    @ApiModelProperty(value = "年月", notes = "null")
    private String monthTime;
}
