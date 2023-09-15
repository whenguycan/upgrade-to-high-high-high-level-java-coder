package com.ruoyi.project.parking.domain.vo;

import com.ruoyi.project.merchant.domain.bo.MonthlyCarRentalOrderBO;
import lombok.Data;

import java.util.List;

@Data
public class SearchMonthlyOrderResponseVO {
    private String status;
    private String mess;
    private Integer pageTotal;
    private Integer pageCurrent;
    List<MonthlyCarRentalOrderBO> orderDetail;
}
