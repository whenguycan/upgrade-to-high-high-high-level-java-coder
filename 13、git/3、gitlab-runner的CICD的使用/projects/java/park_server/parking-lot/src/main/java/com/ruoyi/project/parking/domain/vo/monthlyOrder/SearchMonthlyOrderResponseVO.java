package com.ruoyi.project.parking.domain.vo.monthlyOrder;

import lombok.Data;

import java.util.List;

/**
 * 查询包月订单记录 请求结果
 */
@Data
public class SearchMonthlyOrderResponseVO {

    private String status;

    private String mess;

    private Integer pageTotal;

    private Integer pageCurrent;

    // 返回订单详情
    List<MonthlyOrderVO> orderDetail;
}
