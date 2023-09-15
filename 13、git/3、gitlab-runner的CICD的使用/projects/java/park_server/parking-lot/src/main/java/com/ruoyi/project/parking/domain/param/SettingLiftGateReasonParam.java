package com.ruoyi.project.parking.domain.param;

import com.ruoyi.project.parking.domain.vo.SettingLiftGateReasonVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

/**
 * 闸道抬杆原因
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SettingLiftGateReasonParam extends SettingLiftGateReasonVO {
    @NotNull(message = "原因id不为空")
    private Integer id;

}
