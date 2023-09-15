package com.ruoyi.project.parking.domain.param;

import com.ruoyi.project.parking.domain.vo.WhiteListVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 白名单
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WhiteListParam extends WhiteListVO {
    @NotNull(message = "白名单id不为空")
    private Integer id;
}
