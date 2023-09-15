package com.ruoyi.project.parking.domain.param;

import com.ruoyi.project.parking.domain.vo.RegularCarVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 固定车记录
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RegularCarParam extends RegularCarVO {
    @NotNull(message = "固定车记录id不为空")
    private Integer id;
}
