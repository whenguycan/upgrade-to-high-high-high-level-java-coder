package com.ruoyi.project.parking.domain.bo;


import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class RegularCarBO {

    /**
     * 固定车车牌号
     */
    @Excel(name = "固定车车牌号")
    private String carNumber;

    /**
     * 固定车车辆颜色
     */
    @Excel(name = "固定车车辆颜色")
    private String carColor;

    /**
     * 固定车车牌备注
     */
    @Excel(name = "固定车车牌备注")
    private String carRemark;

    /**
     * 固定车车主身份证号
     */
    @Excel(name = "固定车车主身份证号")
    private String ownerCardId;

    /**
     * 固定车车主姓名
     */
    @Excel(name = "固定车车主姓名")
    private String ownerName;

    /**
     * 固定车车主联系地址
     */
    @Excel(name = "固定车车主联系地址")
    private String ownerAddress;

    /**
     * 固定车车主联系电话
     */
    @Excel(name = "固定车车主联系电话")
    private String ownerPhone;

    /**
     * 流动车位数
     */
    @Excel(name = "流动车位数")
    private Integer flowPlaceNumber;

    /**
     * 有效期开始时间
     */
    @Excel(name = "有效期开始时间", dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 有效期结束时间
     */
    @Excel(name = "有效期结束时间", dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;
}
