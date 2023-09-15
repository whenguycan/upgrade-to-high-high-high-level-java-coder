package com.ruoyi.project.parking.domain.vo.monthlyOrder;

import lombok.Data;

/**
 * 查询包月订单记录 请求参数
 */
@Data
public class SearchMonthlyOrderRequestVO {
    private Integer orderUserId ;
    private String orderStatus ;
    private String payStatus ;
    private Integer pageNum ;
    private Integer pageSize ;
    private String orderNo ;

    private String parkNo;
}
