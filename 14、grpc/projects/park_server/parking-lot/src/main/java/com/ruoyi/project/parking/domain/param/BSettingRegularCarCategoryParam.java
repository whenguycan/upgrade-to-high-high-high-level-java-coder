package com.ruoyi.project.parking.domain.param;

import com.ruoyi.project.parking.domain.vo.BSettingRegularCarCategoryVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 编辑固定车类型入参
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BSettingRegularCarCategoryParam extends BSettingRegularCarCategoryVO {
    @NotNull(message = "固定车类型id不为空")
    private Integer id;

}
