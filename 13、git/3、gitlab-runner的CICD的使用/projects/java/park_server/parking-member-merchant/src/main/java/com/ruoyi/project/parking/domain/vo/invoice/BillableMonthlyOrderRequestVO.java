package com.ruoyi.project.parking.domain.vo.invoice;

import lombok.Data;

import java.util.List;

/**
 * 查询 可开票 停车订单信息 - 请求参数
 */
@Data
public class BillableMonthlyOrderRequestVO {
    private Integer orderUserId;
    private Integer pageNum;
    private Integer pageSize;
    //是否查询可开票
    private boolean billable;
}
