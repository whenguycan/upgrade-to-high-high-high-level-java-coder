package com.ruoyi.project.parking.domain.vo.invoice;

import java.util.List;

import lombok.Data;

/**
 * 查询 开票历史记录 - 响应参数
 */
@Data
public class BillRecordResponseVO {
    private String status;
    private String mess;
    private Integer pageTotal;
    private Integer pageCurrent;
    private List<ParkingOrderInvoiceInfoVO> billDetail;
}
