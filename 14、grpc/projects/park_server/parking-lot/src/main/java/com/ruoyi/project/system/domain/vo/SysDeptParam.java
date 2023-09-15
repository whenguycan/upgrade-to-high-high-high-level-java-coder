package com.ruoyi.project.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "场库信息",description = "")
@Data
public class SysDeptParam {
    private Long deptId;
    @ApiModelProperty(value = "部门状态")
    private String status;
}
