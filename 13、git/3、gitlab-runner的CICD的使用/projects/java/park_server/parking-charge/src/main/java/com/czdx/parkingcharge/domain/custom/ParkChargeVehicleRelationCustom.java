package com.czdx.parkingcharge.domain.custom;

import com.czdx.parkingcharge.domain.ParkChargeRelationVehicle;
import lombok.Data;

import java.util.List;

/**
 *
 * description: 车场计费车型关系扩展类
 * @author mingchenxu
 * @date 2023/3/31 14:08
 */
@Data
public class ParkChargeVehicleRelationCustom {

    /**
     * 车场编号
     */
    private String parkNo;

    /**
     * 关系集合
     */
    private List<ParkChargeRelationVehicle> relations;

}
