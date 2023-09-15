package com.ruoyi.project.parking.domain.param;

import com.ruoyi.project.parking.domain.vo.TExemptOrderVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 减免订单 参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TExemptOrderParam extends TExemptOrderVO {
    @NotNull(message = "减免订单id不为空")
    private Integer id;
}
