package com.ruoyi.project.parking.domain.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 在场车辆 更新参数
 */
@Data
public class ParkLiveRecordsUpdateParam {
    /**
     * 在场车辆 id
     */
    @NotNull(message = "id不为空")
    private Integer id;

    /**
     * 车牌号
     */
    @NotBlank(message = "车牌号不为空")
    private String carNumber;
}
