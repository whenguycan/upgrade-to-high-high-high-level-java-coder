package com.ruoyi.project.parking.domain.param;

import com.ruoyi.project.parking.domain.vo.SettingCarTypeVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 车辆类型
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SettingCarTypeParam extends SettingCarTypeVO {
    @NotNull(message = "车辆类型id不为空")
    private Integer id;

}
