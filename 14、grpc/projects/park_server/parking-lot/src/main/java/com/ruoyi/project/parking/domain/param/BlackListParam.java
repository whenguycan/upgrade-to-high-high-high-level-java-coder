package com.ruoyi.project.parking.domain.param;

import com.ruoyi.project.parking.domain.vo.WhiteListVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 编辑黑名单入参
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BlackListParam extends WhiteListVO {
    @NotNull(message = "黑名单id不为空")
    private Integer id;
}
