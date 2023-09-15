package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class RegularCarVO {

    /**
     * 固定车车牌号
     */
    @NotNull(message = "车牌号不能为空")
    private String carNumber;

    /**
     * 固定车车辆颜色
     */
    private String carColor;

    /**
     * 固定车车牌备注
     */
    private String carRemark;

    /**
     * 固定车类型
     */
    private Integer carCategoryId;

    /**
     * 固定车车主编号
     */
    @NotNull(message = "车主身份证号不能为空")
    private String ownerCardId;

    /**
     * 固定车车主姓名
     */
    private String ownerName;

    /**
     * 固定车车主联系地址
     */
    private String ownerAddress;

    /**
     * 固定车车主联系电话
     */
    private String ownerPhone;

    /**
     * 流动车位数
     */
    @Min(value = 0,message = "流动车位数不能为负")
    private Integer flowPlaceNumber;

    /**
     * 有效期开始时间
     */
    private LocalDate startTime;

    /**
     * 有效期结束时间
     */
    private LocalDate endTime;

    /**
     * 永久时限  '0'-临时 '1'-永久
     */
    private String timeLimit;

    /**
     * 备注
     */
    private String remark;
}
