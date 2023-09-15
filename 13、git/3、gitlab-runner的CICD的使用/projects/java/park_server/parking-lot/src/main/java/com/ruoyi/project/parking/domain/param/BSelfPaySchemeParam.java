package com.ruoyi.project.parking.domain.param;

import com.ruoyi.project.parking.domain.vo.BSelfPaySchemeVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 编辑自主缴费方案入参
 * </p>
 *
 * @author 琴声何来
 * @since 2023/3/1 10:06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BSelfPaySchemeParam extends BSelfPaySchemeVO {
    @NotNull(message = "自主缴费方案id不为空")
    private Integer id;
}
