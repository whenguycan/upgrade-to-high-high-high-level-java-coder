package com.ruoyi.project.parking.domain.vo.parkingorder;

import lombok.Data;

import java.util.List;

@Data
public class SearchRefundOrderRecordsResponseVO {
    private String status;
    private String mess;
    private Integer pageTotal;
    private Integer pageCurrent;
    List<RefundOrderRecordsVO> orderDetail;
}
