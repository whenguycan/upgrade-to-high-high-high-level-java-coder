package com.ruoyi.project.parking.domain.param;

import com.ruoyi.project.parking.domain.vo.BSettingRegularCarCategoryPriceVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 编辑固定车价格标准入参
 * </p>
 *
 * @author 琴声何来
 * @since 2023/2/24 16:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BSettingRegularCarCategoryPriceParam extends BSettingRegularCarCategoryPriceVO {
    @NotNull(message = "固定车类型id不为空")
    private Integer id;
}
