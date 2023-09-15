package com.ruoyi.project.parking.domain.vo;


import lombok.Data;


/**
 * <p>
 * 自主缴费审核 入参
 * </p>
 *
 * @author 琴声何来
 * @since 2023/3/1 10:43
 */
@Data
public class SelfPayVO {

    /**
     * 固定车类型id
     */
    private Integer regularCarCategoryId;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 车主姓名
     */
    private String ownerName;

    /**
     * 车主联系方式
     */
    private String ownerPhone;

    /**
     * 车主类型 '0'-新 '1'-老
     */
    private String ownerType;

    /**
     * 续费天数
     */
    private Integer renewDays;
}
