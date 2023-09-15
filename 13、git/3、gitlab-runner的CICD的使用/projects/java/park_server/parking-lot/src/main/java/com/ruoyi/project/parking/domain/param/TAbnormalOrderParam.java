package com.ruoyi.project.parking.domain.param;

import com.ruoyi.project.parking.domain.vo.TAbnormalOrderVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 异常订单 参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TAbnormalOrderParam extends TAbnormalOrderVO {
    @NotNull(message = "异常订单id不为空")
    private Integer id;
}
