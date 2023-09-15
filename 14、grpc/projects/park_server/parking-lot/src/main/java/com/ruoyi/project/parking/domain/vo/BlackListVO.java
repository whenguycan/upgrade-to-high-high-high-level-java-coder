package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 黑名单 新增传参
 * </p>
 *
 * @author 琴声何来
 * @since 2023/2/28 8:55
 */
@Data
public class BlackListVO {

    /**
     * 黑名单车牌号
     */
    @NotNull(message = "车牌号不能为空")
    private String carNumber;


    /**
     * 备注
     */
    private String remark;
}
