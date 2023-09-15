package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class SearchOrderResponseVO {
    private String status;
    private String mess;
    private Integer pageTotal;
    private Integer pageCurrent;
    List<VehicleParkOrderVO> orderDetail;
}
