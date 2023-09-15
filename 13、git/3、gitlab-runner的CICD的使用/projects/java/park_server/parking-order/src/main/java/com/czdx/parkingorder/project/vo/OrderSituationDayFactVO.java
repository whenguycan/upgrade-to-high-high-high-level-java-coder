package com.czdx.parkingorder.project.vo;

import lombok.Data;

@Data
public class OrderSituationDayFactVO {

    /** 统计日期 */
    private String day;

    /** 订单数 */
    private Integer count;
}
