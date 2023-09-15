package com.ruoyi.project.parking.domain.vo;


import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 * 固定车收费标准入参
 * </p>
 *
 * @author 琴声何来
 * @since 2023/2/24 16:32
 */
@Data
public class BSettingRegularCarCategoryPriceVO {

    /**
     * 固定车类型id
     */
    @NotNull(message = "固定车类型id不为空")
    private Integer regularCarCategoryId;

    /**
     * 月数
     */
    @NotNull(message = "月数不为空")
    @Min(value = 1, message = "月数不能为负")
    private Integer month;

    /**
     * 价格
     */
    @NotNull(message = "价格不为空")
    @Min(value = 1, message = "价格不能为负")
    private BigDecimal price;
}
