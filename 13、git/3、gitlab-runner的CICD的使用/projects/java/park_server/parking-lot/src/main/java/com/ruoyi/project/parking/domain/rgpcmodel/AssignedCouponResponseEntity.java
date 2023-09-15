package com.ruoyi.project.parking.domain.rgpcmodel;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AssignedCouponResponseEntity implements Serializable {
    private String mess;
    private String status;
    private List<AssignedCoupon> itemList;
}
