package com.ruoyi.project.parking.domain.param;

import com.ruoyi.project.parking.domain.vo.TDeductionOrderVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 抵扣订单 参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TDeductionOrderParam extends TDeductionOrderVO {
    @NotNull(message = "抵扣订单id不为空")
    private Integer id;
}
